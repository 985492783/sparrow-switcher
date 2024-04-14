/*
 * Copyright 1999-2020 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.switcher.common.remote.client;

import com.sparrow.switcher.common.remote.Closeable;
import com.sparrow.switcher.common.remote.ConnectionType;
import com.sparrow.switcher.common.remote.PayloadRegistry;
import com.sparrow.switcher.common.remote.exception.SparrowException;
import com.sparrow.switcher.common.remote.request.ClientDetectionRequest;
import com.sparrow.switcher.common.remote.request.ConnectResetRequest;
import com.sparrow.switcher.common.remote.request.Request;
import com.sparrow.switcher.common.remote.response.ClientDetectionResponse;
import com.sparrow.switcher.common.remote.response.ConnectResetResponse;
import com.sparrow.switcher.common.remote.response.Response;
import com.sparrow.switcher.common.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RPC Client.
 *
 * @author pixel-revolve
 */
public abstract class RpcClient implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger("com.sparrow.switcher.common.remote.client");

    private ServerListFactory serverListFactory;

    protected BlockingQueue<ConnectionEvent> eventLinkedBlockingQueue = new LinkedBlockingQueue<>();

    protected volatile AtomicReference<RpcClientStatus> rpcClientStatus = new AtomicReference<>(
            RpcClientStatus.WAIT_INIT);

    protected RpcClientConfig rpcClientConfig;

    protected volatile Connection currentConnection;

    private final BlockingQueue<ReconnectContext> reconnectionSignal = new ArrayBlockingQueue<>(1);

    private static final Pattern EXCLUDE_PROTOCOL_PATTERN = Pattern.compile("(?<=\\w{1,5}://)(.*)");

    private long lastActiveTimeStamp = System.currentTimeMillis();

    /**
     * handlers to
     */
    protected List<ServerRequestHandler> serverRequestHandlers = new ArrayList<>();

    static {
        PayloadRegistry.init();
    }

    public RpcClient(RpcClientConfig rpcClientConfig) {
        this(rpcClientConfig, null);
    }

    public RpcClient(RpcClientConfig rpcClientConfig, ServerListFactory serverListFactory) {
        this.rpcClientConfig = rpcClientConfig;
        this.serverListFactory = serverListFactory;
        init();
    }

    protected void init() {
        if (this.serverListFactory != null) {
            rpcClientStatus.compareAndSet(RpcClientStatus.WAIT_INIT, RpcClientStatus.INITIALIZED);
            LoggerUtils.printIfInfoEnabled(LOGGER, "RpcClient init in constructor, ServerListFactory = {}",
                    serverListFactory.getClass().getName());
        }
    }

    /**
     * Start this client
     *
     * @throws SparrowException
     */
    public final void start() throws SparrowException {

        boolean success = rpcClientStatus.compareAndSet(RpcClientStatus.INITIALIZED, RpcClientStatus.STARTING);
        if (!success) {
            return;
        }

        Connection connectToServer = null;
        rpcClientStatus.set(RpcClientStatus.STARTING);

        int startUpRetryTimes = rpcClientConfig.retryTimes();
        while (startUpRetryTimes >= 0 && connectToServer == null) {
            try {
                startUpRetryTimes--;
                ServerInfo serverInfo = nextRpcServer();

                LoggerUtils.printIfInfoEnabled(LOGGER, "[{}] Try to connect to server on start up, server: {}",
                        rpcClientConfig.name(), serverInfo);
                //执行连接
                connectToServer = connectToServer(serverInfo);
            } catch (Throwable e) {
                LoggerUtils.printIfWarnEnabled(LOGGER,
                        "[{}] Fail to connect to server on start up, error message = {}, start up retry times left: {}",
                        rpcClientConfig.name(), e.getMessage(), startUpRetryTimes, e);
            }

        }

        //如果连接成功
        if (connectToServer != null) {
            LoggerUtils
                    .printIfInfoEnabled(LOGGER, "[{}] Success to connect to server [{}] on start up, connectionId = {}",
                            rpcClientConfig.name(), connectToServer.serverInfo.getAddress(),
                            connectToServer.getConnectionId());
            this.currentConnection = connectToServer;
            rpcClientStatus.set(RpcClientStatus.RUNNING);
            eventLinkedBlockingQueue.offer(new ConnectionEvent(ConnectionEvent.CONNECTED, currentConnection));
        } else {
            switchServerAsync();
        }

        registerServerRequestHandler(new ConnectResetRequestHandler());

        // register client detection request.
        registerServerRequestHandler((request, connection) -> {
            if (request instanceof ClientDetectionRequest) {
                return new ClientDetectionResponse();
            }

            return null;
        });
    }

    class ConnectResetRequestHandler implements ServerRequestHandler {

        @Override
        public Response requestReply(Request request, Connection connection) {

            if (request instanceof ConnectResetRequest) {

                try {
                    synchronized (RpcClient.this) {
                        if (isRunning()) {
                            ConnectResetRequest connectResetRequest = (ConnectResetRequest) request;
                            if (StringUtils.isNotBlank(connectResetRequest.getServerIp())) {
                                ServerInfo serverInfo = resolveServerInfo(
                                        connectResetRequest.getServerIp() + ":" + connectResetRequest
                                                .getServerPort());
                                switchServerAsync(serverInfo, false);
                            } else {
                                switchServerAsync();
                            }
                            afterReset(connectResetRequest);
                        }
                    }
                } catch (Exception e) {
                    LoggerUtils.printIfErrorEnabled(LOGGER, "[{}] Switch server error, {}", rpcClientConfig.name(), e);
                }
                return new ConnectResetResponse();
            }
            return null;
        }
    }

    public boolean isRunning() {
        return this.rpcClientStatus.get() == RpcClientStatus.RUNNING;
    }

    protected void afterReset(ConnectResetRequest request) {
        // hook for GrpcClient
    }

    @Override
    public void shutdown() throws SparrowException {
        LOGGER.info("Shutdown rpc client, set status to shutdown");
        rpcClientStatus.set(RpcClientStatus.SHUTDOWN);
        closeConnection(currentConnection);
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            LOGGER.info("Close current connection " + connection.getConnectionId());
            connection.close();
            eventLinkedBlockingQueue.add(new ConnectionEvent(ConnectionEvent.DISCONNECTED, connection));
        }
    }

    public abstract ConnectionType getConnectionType();

    public abstract int rpcPortOffset();

    public abstract Connection connectToServer(ServerInfo serverInfo) throws Exception;

    protected ServerInfo nextRpcServer() {
        String serverAddress = getServerListFactory().genNextServer();
        return resolveServerInfo(serverAddress);
    }

    public ServerListFactory getServerListFactory() {
        return serverListFactory;
    }

    private ServerInfo resolveServerInfo(String serverAddress) {
        Matcher matcher = EXCLUDE_PROTOCOL_PATTERN.matcher(serverAddress);
        if (matcher.find()) {
            serverAddress = matcher.group(1);
        }
        String[] ipPortTuple = InternetAddressUtil.splitIPPortStr(serverAddress);
        //todo:设置默认服务器
        int defaultPort = Integer.parseInt(System.getProperty("sparrow.server.port", "8848"));
        String serverPort = CollectionUtils.getOrDefault(ipPortTuple, 1, Integer.toString(defaultPort));

        return new ServerInfo(ipPortTuple[0], NumberUtils.toInt(serverPort, defaultPort));
    }

    public void switchServerAsync() {
        switchServerAsync(null, false);
    }

    protected void switchServerAsync(final ServerInfo recommendServerInfo, boolean onRequestFail) {
        reconnectionSignal.offer(new ReconnectContext(recommendServerInfo, onRequestFail));
    }

    public synchronized void registerServerRequestHandler(ServerRequestHandler serverRequestHandler) {
        LoggerUtils.printIfInfoEnabled(LOGGER, "[{}] Register server push request handler:{}", rpcClientConfig.name(),
                serverRequestHandler.getClass().getName());

        this.serverRequestHandlers.add(serverRequestHandler);
    }

    protected Response handleServerRequest(final Request request) {

        LoggerUtils.printIfInfoEnabled(LOGGER, "[{}] Receive server push request, request = {}, requestId = {}",
                rpcClientConfig.name(), request.getClass().getSimpleName(), request.getRequestId());
        lastActiveTimeStamp = System.currentTimeMillis();
        for (ServerRequestHandler serverRequestHandler : serverRequestHandlers) {
            try {
                Response response = serverRequestHandler.requestReply(request, currentConnection);

                if (response != null) {
                    LoggerUtils.printIfInfoEnabled(LOGGER, "[{}] Ack server push request, request = {}, requestId = {}",
                            rpcClientConfig.name(), request.getClass().getSimpleName(), request.getRequestId());
                    return response;
                }
            } catch (Exception e) {
                LoggerUtils.printIfInfoEnabled(LOGGER, "[{}] HandleServerRequest:{}, errorMessage = {}",
                        rpcClientConfig.name(), serverRequestHandler.getClass().getName(), e.getMessage());
                throw e;
            }

        }
        return null;
    }

    public static class ServerInfo {

        protected String serverIp;

        protected int serverPort;

        public ServerInfo() {

        }

        public ServerInfo(String serverIp, int serverPort) {
            this.serverPort = serverPort;
            this.serverIp = serverIp;
        }

        /**
         * get address, ip:port.
         *
         * @return address.
         */
        public String getAddress() {
            return serverIp + ":" + serverPort;
        }

        /**
         * Setter method for property <tt>serverIp</tt>.
         *
         * @param serverIp value to be assigned to property serverIp
         */
        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }

        /**
         * Setter method for property <tt>serverPort</tt>.
         *
         * @param serverPort value to be assigned to property serverPort
         */
        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }

        /**
         * Getter method for property <tt>serverIp</tt>.
         *
         * @return property value of serverIp
         */
        public String getServerIp() {
            return serverIp;
        }

        /**
         * Getter method for property <tt>serverPort</tt>.
         *
         * @return property value of serverPort
         */
        public int getServerPort() {
            return serverPort;
        }

        @Override
        public String toString() {
            return "{serverIp = '" + serverIp + '\'' + ", server main port = " + serverPort + '}';
        }
    }

    public static class ConnectionEvent {

        public static final int CONNECTED = 1;

        public static final int DISCONNECTED = 0;

        int eventType;

        Connection connection;

        public ConnectionEvent(int eventType, Connection connection) {
            this.eventType = eventType;
            this.connection = connection;
        }

        public boolean isConnected() {
            return eventType == CONNECTED;
        }

        public boolean isDisConnected() {
            return eventType == DISCONNECTED;
        }
    }

    static class ReconnectContext {

        public ReconnectContext(ServerInfo serverInfo, boolean onRequestFail) {
            this.onRequestFail = onRequestFail;
            this.serverInfo = serverInfo;
        }

        boolean onRequestFail;

        ServerInfo serverInfo;
    }

}
