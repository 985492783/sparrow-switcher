package com.sparrow.switcher.common.remote.client.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import com.sparrow.switcher.common.grpc.auto.BiRequestStreamGrpc;
import com.sparrow.switcher.common.grpc.auto.Payload;
import com.sparrow.switcher.common.grpc.auto.RequestGrpc;
import com.sparrow.switcher.common.packagescan.resource.DefaultResourceLoader;
import com.sparrow.switcher.common.packagescan.resource.Resource;
import com.sparrow.switcher.common.packagescan.resource.ResourceLoader;
import com.sparrow.switcher.common.remote.ConnectionType;
import com.sparrow.switcher.common.remote.PayloadRegistry;
import com.sparrow.switcher.common.remote.client.*;
import com.sparrow.switcher.common.remote.constants.AbilityMode;
import com.sparrow.switcher.common.remote.exception.SparrowException;
import com.sparrow.switcher.common.remote.request.ConnectionSetupRequest;
import com.sparrow.switcher.common.remote.request.Request;
import com.sparrow.switcher.common.remote.request.ServerCheckRequest;
import com.sparrow.switcher.common.remote.request.SetupAckRequest;
import com.sparrow.switcher.common.remote.response.ErrorResponse;
import com.sparrow.switcher.common.remote.response.Response;
import com.sparrow.switcher.common.remote.response.ServerCheckResponse;
import com.sparrow.switcher.common.remote.response.SetupAckResponse;
import com.sparrow.switcher.common.utils.*;
import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * gRPC Client.
 *
 * @author pixel-revolve
 */
public abstract class GrpcClient extends RpcClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcClient.class);

    private static final String SPARROW_SERVER_GRPC_PORT_DEFAULT_OFFSET = "1000";

    private final GrpcClientConfig clientConfig;

    private final RecAbilityContext recAbilityContext = new RecAbilityContext(null);

    private SetupRequestHandler setupRequestHandler;

    protected final ResourceLoader resourceLoader = new DefaultResourceLoader();

    static {
        PayloadRegistry.init();
    }

    public GrpcClient(GrpcClientConfig clientConfig) {
        super(clientConfig);
        this.clientConfig = clientConfig;
        initSetupHandler();
    }

    private void initSetupHandler() {
        // register to handler setup request
        setupRequestHandler = new SetupRequestHandler(this.recAbilityContext);
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.GRPC;
    }

    @Override
    public Connection connectToServer(ServerInfo serverInfo) {
        // the newest connection id
        String connectionId = "";
        try {
            int port = serverInfo.getServerPort() + rpcPortOffset();
            ManagedChannel managedChannel = createNewManagedChannel(serverInfo.getServerIp(), port);
            RequestGrpc.RequestFutureStub newChannelStubTemp = createNewChannelStub(managedChannel);

            //检查服务端
            Response response = serverCheck(serverInfo.getServerIp(), port, newChannelStubTemp);
            if (!(response instanceof ServerCheckResponse)) {
                shuntDownChannel(managedChannel);
                return null;
            }
            // submit ability table as soon as possible
            // ability table will be null if server doesn't support ability table
            ServerCheckResponse serverCheckResponse = (ServerCheckResponse) response;
            connectionId = serverCheckResponse.getConnectionId();

            BiRequestStreamGrpc.BiRequestStreamStub biRequestStreamStub = BiRequestStreamGrpc
                    .newStub(newChannelStubTemp.getChannel());
            GrpcConnection grpcConn = new GrpcConnection(serverInfo);
            grpcConn.setConnectionId(connectionId);
            // if not supported, it will be false
            if (serverCheckResponse.isSupportAbilityNegotiation()) {
                // mark
                this.recAbilityContext.reset(grpcConn);
                // promise null if no abilities receive
                grpcConn.setAbilityTable(null);
            }

            //create stream request and bind connection event to this connection.
            StreamObserver<Payload> payloadStreamObserver = bindRequestStream(biRequestStreamStub, grpcConn);

            // stream observer to send response to server
            grpcConn.setPayloadStreamObserver(payloadStreamObserver);
            grpcConn.setGrpcFutureServiceStub(newChannelStubTemp);
            grpcConn.setChannel(managedChannel);
            //send a  setup request.
            ConnectionSetupRequest conSetupRequest = new ConnectionSetupRequest();
            conSetupRequest.setClientVersion(VersionUtils.getFullClientVersion());
            // set ability table
//            conSetupRequest
//                    .setAbilityTable(SparrowAbilityManagerHolder.getInstance().getCurrentNodeAbilities(abilityMode()));
//            conSetupRequest.setTenant(super.getTenant());
            //发起建立连接请求
            grpcConn.sendRequest(conSetupRequest);
            // wait for response
            if (recAbilityContext.isNeedToSync()) {
                // try to wait for notify response
                recAbilityContext.await(this.clientConfig.capabilityNegotiationTimeout(), TimeUnit.MILLISECONDS);
                // if no server abilities receiving, then reconnect
                if (!recAbilityContext.check(grpcConn)) {
                    return null;
                }
            } else {
                // leave for adapting old version server
                // registration is considered successful by default after 100ms
                // wait to register connection setup
                Thread.sleep(100L);
            }
            return grpcConn;
        } catch (Exception e) {
            LOGGER.error("[{}]Fail to connect to server!,error={}", GrpcClient.this.getName(), e);
            // remove and notify
            recAbilityContext.release(null);
        }
        return null;
    }

    public RequestGrpc.RequestFutureStub createNewChannelStub(ManagedChannel managedChannelTemp) {
        return RequestGrpc.newFutureStub(managedChannelTemp);
    }

    private ManagedChannel createNewManagedChannel(String serverIp, int serverPort) {
        LOGGER.info("grpc client connection server:{} ip,serverPort:{},grpcTslConfig:{}", serverIp, serverPort,
                JacksonUtils.toJson(clientConfig.tlsConfig()));
        ManagedChannelBuilder<?> managedChannelBuilder = buildChannel(serverIp, serverPort, buildSslContext())
                .compressorRegistry(CompressorRegistry.getDefaultInstance())
                .decompressorRegistry(DecompressorRegistry.getDefaultInstance())
                .maxInboundMessageSize(clientConfig.maxInboundMessageSize())
                .keepAliveTime(clientConfig.channelKeepAlive(), TimeUnit.MILLISECONDS)
                .keepAliveTimeout(clientConfig.channelKeepAliveTimeout(), TimeUnit.MILLISECONDS);
        return managedChannelBuilder.build();
    }

    private ManagedChannelBuilder buildChannel(String serverIp, int port, Optional<SslContext> sslContext) {
        if (sslContext.isPresent()) {
            return NettyChannelBuilder.forAddress(serverIp, port).negotiationType(NegotiationType.TLS)
                    .sslContext(sslContext.get());

        } else {
            return ManagedChannelBuilder.forAddress(serverIp, port).usePlaintext();
        }
    }

    public String getName() {
        return rpcClientConfig.name();
    }

    @Override
    public void shutdown() throws SparrowException {
        super.shutdown();
//        if (grpcExecutor != null) {
//            LOGGER.info("Shutdown grpc executor " + grpcExecutor);
//            grpcExecutor.shutdown();
//        }
    }

    private void shuntDownChannel(ManagedChannel managedChannel) {
        if (managedChannel != null && !managedChannel.isShutdown()) {
            managedChannel.shutdownNow();
        }
    }

    private Response serverCheck(String ip, int port, RequestGrpc.RequestFutureStub requestBlockingStub) {
        try {
            ServerCheckRequest serverCheckRequest = new ServerCheckRequest();
            Payload grpcRequest = GrpcUtils.convert(serverCheckRequest);
            ListenableFuture<Payload> responseFuture = requestBlockingStub.request(grpcRequest);
            Payload response = responseFuture.get(clientConfig.serverCheckTimeOut(), TimeUnit.MILLISECONDS);
            //receive connection unregister response here,not check response is success.
            return (Response) GrpcUtils.parse(response);
        } catch (Exception e) {
            LoggerUtils.printIfErrorEnabled(LOGGER,
                    "Server check fail, please check server {} ,port {} is available , error ={}", ip, port, e);
            if (this.clientConfig != null && this.clientConfig.tlsConfig() != null && this.clientConfig.tlsConfig()
                    .getEnableTls()) {
                LoggerUtils.printIfErrorEnabled(LOGGER,
                        "current client is require tls encrypted ,server must support tls ,please check");
            }
            return null;
        }
    }

    private StreamObserver<Payload> bindRequestStream(final BiRequestStreamGrpc.BiRequestStreamStub streamStub,
                                                      final GrpcConnection grpcConn) {

        return streamStub.requestBiStream(new StreamObserver<Payload>() {

            @Override
            public void onNext(Payload payload) {

                LoggerUtils.printIfDebugEnabled(LOGGER, "[{}]Stream server request receive, original info: {}",
                        grpcConn.getConnectionId(), payload.toString());
                try {
                    Object parseBody = GrpcUtils.parse(payload);
                    final Request request = (Request) parseBody;
                    if (request != null) {

                        try {
                            if (request instanceof SetupAckRequest) {
                                // there is no connection ready this time
                                setupRequestHandler.requestReply(request, null);
                                return;
                            }
                            Response response = handleServerRequest(request);
                            if (response != null) {
                                response.setRequestId(request.getRequestId());
                                sendResponse(response);
                            } else {
                                LOGGER.warn("[{}]Fail to process server request, ackId->{}", grpcConn.getConnectionId(),
                                        request.getRequestId());
                            }

                        } catch (Exception e) {
                            LoggerUtils.printIfErrorEnabled(LOGGER, "[{}]Handle server request exception: {}",
                                    grpcConn.getConnectionId(), payload.toString(), e.getMessage());
                            Response errResponse = ErrorResponse
                                    .build(SparrowException.CLIENT_ERROR, "Handle server request error");
                            errResponse.setRequestId(request.getRequestId());
                            sendResponse(errResponse);
                        }

                    }

                } catch (Exception e) {

                    LoggerUtils.printIfErrorEnabled(LOGGER, "[{}]Error to process server push response: {}",
                            grpcConn.getConnectionId(), payload.getBody().getValue().toStringUtf8());
                    // remove and notify
                    recAbilityContext.release(null);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                boolean isRunning = isRunning();
                boolean isAbandon = grpcConn.isAbandon();
                if (isRunning && !isAbandon) {
                    LoggerUtils.printIfErrorEnabled(LOGGER, "[{}]Request stream error, switch server,error={}",
                            grpcConn.getConnectionId(), throwable);
                    if (rpcClientStatus.compareAndSet(RpcClientStatus.RUNNING, RpcClientStatus.UNHEALTHY)) {
                        switchServerAsync();
                    }

                } else {
                    LoggerUtils.printIfWarnEnabled(LOGGER, "[{}]Ignore error event,isRunning:{},isAbandon={}",
                            grpcConn.getConnectionId(), isRunning, isAbandon);
                }

            }

            @Override
            public void onCompleted() {
                boolean isRunning = isRunning();
                boolean isAbandon = grpcConn.isAbandon();
                if (isRunning && !isAbandon) {
                    LoggerUtils.printIfErrorEnabled(LOGGER, "[{}]Request stream onCompleted, switch server",
                            grpcConn.getConnectionId());
                    if (rpcClientStatus.compareAndSet(RpcClientStatus.RUNNING, RpcClientStatus.UNHEALTHY)) {
                        switchServerAsync();
                    }

                } else {
                    LoggerUtils.printIfInfoEnabled(LOGGER, "[{}]Ignore complete event,isRunning:{},isAbandon={}",
                            grpcConn.getConnectionId(), isRunning, isAbandon);
                }

            }
        });
    }

    private void sendResponse(Response response) {
        try {
            ((GrpcConnection) this.currentConnection).sendResponse(response);
        } catch (Exception e) {
            LOGGER.error("[{}]Error to send ack response, ackId->{}", this.currentConnection.getConnectionId(),
                    response.getRequestId());
        }
    }

    protected abstract AbilityMode abilityMode();

    /**
     * This is for receiving server abilities.
     */
    static class RecAbilityContext {

        /**
         * connection waiting for server abilities.
         */
        private volatile Connection connection;

        /**
         * way to block client.
         */
        private volatile CountDownLatch blocker;

        private volatile boolean needToSync = false;

        public RecAbilityContext(Connection connection) {
            this.connection = connection;
            this.blocker = new CountDownLatch(1);
        }

        /**
         * whether to sync for ability table.
         *
         * @return whether to sync for ability table.
         */
        public boolean isNeedToSync() {
            return this.needToSync;
        }

        /**
         * reset with new connection which is waiting for ability table.
         *
         * @param connection new connection which is waiting for ability table.
         */
        public void reset(Connection connection) {
            this.connection = connection;
            this.blocker = new CountDownLatch(1);
            this.needToSync = true;
        }

        /**
         * notify sync by abilities.
         *
         * @param abilities abilities.
         */
        public void release(Map<String, Boolean> abilities) {
            if (this.connection != null) {
                this.connection.setAbilityTable(abilities);
                // avoid repeat setting
                this.connection = null;
            }
            if (this.blocker != null) {
                blocker.countDown();
            }
            this.needToSync = false;
        }

        /**
         * await for abilities.
         *
         * @param timeout timeout.
         * @param unit    unit.
         * @throws InterruptedException by blocker.
         */
        public void await(long timeout, TimeUnit unit) throws InterruptedException {
            if (this.blocker != null) {
                this.blocker.await(timeout, unit);
            }
            this.needToSync = false;
        }

        /**
         * check whether receive abilities.
         *
         * @param connection conn.
         * @return whether receive abilities.
         */
        public boolean check(Connection connection) {
            if (!connection.isAbilitiesSet()) {
                LOGGER.error(
                        "Client don't receive server abilities table even empty table but server supports ability negotiation."
                                + " You can check if it is need to adjust the timeout of ability negotiation by property: {}"
                                + " if always fail to connect.",
                        GrpcConstants.GRPC_CHANNEL_CAPABILITY_NEGOTIATION_TIMEOUT);
                connection.setAbandon(true);
                connection.close();
                return false;
            }
            return true;
        }
    }

    /**
     * Setup response handler.
     */
    class SetupRequestHandler implements ServerRequestHandler {

        private final RecAbilityContext abilityContext;

        public SetupRequestHandler(RecAbilityContext abilityContext) {
            this.abilityContext = abilityContext;
        }

        @Override
        public Response requestReply(Request request, Connection connection) {
            // if finish setup
            if (request instanceof SetupAckRequest) {
                SetupAckRequest setupAckRequest = (SetupAckRequest) request;
                // remove and count down
                recAbilityContext
                        .release(Optional.ofNullable(setupAckRequest.getAbilityTable()).orElse(new HashMap<>(0)));
                return new SetupAckResponse();
            }
            return null;
        }
    }

    private Optional<SslContext> buildSslContext() {

        RpcClientTlsConfig tlsConfig = clientConfig.tlsConfig();
        if (!tlsConfig.getEnableTls()) {
            return Optional.empty();
        }
        try {
            SslContextBuilder builder = GrpcSslContexts.forClient();
            if (StringUtils.isNotBlank(tlsConfig.getSslProvider())) {
                builder.sslProvider(TlsTypeResolve.getSslProvider(tlsConfig.getSslProvider()));
            }

            if (StringUtils.isNotBlank(tlsConfig.getProtocols())) {
                builder.protocols(tlsConfig.getProtocols().split(","));
            }
            if (StringUtils.isNotBlank(tlsConfig.getCiphers())) {
                builder.ciphers(Arrays.asList(tlsConfig.getCiphers().split(",")));
            }
            if (tlsConfig.getTrustAll()) {
                builder.trustManager(InsecureTrustManagerFactory.INSTANCE);
            } else {
                if (StringUtils.isBlank(tlsConfig.getTrustCollectionCertFile())) {
                    throw new IllegalArgumentException("trustCollectionCertFile must be not null");
                }
                Resource resource = resourceLoader.getResource(tlsConfig.getTrustCollectionCertFile());
                builder.trustManager(resource.getInputStream());
            }

            if (tlsConfig.getMutualAuthEnable()) {
                if (StringUtils.isBlank(tlsConfig.getCertChainFile()) || StringUtils
                        .isBlank(tlsConfig.getCertPrivateKey())) {
                    throw new IllegalArgumentException("client certChainFile or certPrivateKey must be not null");
                }
                Resource certChainFile = resourceLoader.getResource(tlsConfig.getCertChainFile());
                Resource privateKey = resourceLoader.getResource(tlsConfig.getCertPrivateKey());
                builder.keyManager(certChainFile.getInputStream(), privateKey.getInputStream(),
                        tlsConfig.getCertPrivateKeyPassword());
            }
            return Optional.of(builder.build());
        } catch (Exception e) {
            throw new RuntimeException("Unable to build SslContext", e);
        }
    }

}
