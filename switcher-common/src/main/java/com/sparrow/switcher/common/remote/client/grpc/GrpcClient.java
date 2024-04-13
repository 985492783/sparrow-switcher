package com.sparrow.switcher.common.remote.client.grpc;

import com.sparrow.switcher.common.remote.ConnectionType;
import com.sparrow.switcher.common.remote.Payload;
import com.sparrow.switcher.common.remote.client.RpcClient;
import com.sparrow.switcher.common.remote.exception.SparrowException;
import com.sparrow.switcher.common.remote.client.Connection;
import com.sparrow.switcher.common.remote.response.Response;
import com.sparrow.switcher.common.remote.utils.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class GrpcClient{
        //extends RpcClient {

//    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcClient.class);
//
//    private static final String SPARROW_SERVER_GRPC_PORT_DEFAULT_OFFSET = "1000";
//
//    @Override
//    public ConnectionType getConnectionType() {
//        return ConnectionType.GRPC;
//    }
//
//    @Override
//    public Connection connectToServer(ServerInfo serverInfo) {
//        // the newest connection id
//        String connectionId = "";
//        try {
//
//            int port = serverInfo.getServerPort() + rpcPortOffset();
//            ManagedChannel managedChannel = createNewManagedChannel(serverInfo.getServerIp(), port);
//            RequestGrpc.RequestFutureStub newChannelStubTemp = createNewChannelStub(managedChannel);
//
//            Response response = serverCheck(serverInfo.getServerIp(), port, newChannelStubTemp);
//            if (!(response instanceof ServerCheckResponse)) {
//                shuntDownChannel(managedChannel);
//                return null;
//            }
//            // submit ability table as soon as possible
//            // ability table will be null if server doesn't support ability table
//            ServerCheckResponse serverCheckResponse = (ServerCheckResponse) response;
//            connectionId = serverCheckResponse.getConnectionId();
//
//            BiRequestStreamGrpc.BiRequestStreamStub biRequestStreamStub = BiRequestStreamGrpc
//                    .newStub(newChannelStubTemp.getChannel());
//            GrpcConnection grpcConn = new GrpcConnection(serverInfo, grpcExecutor);
//            grpcConn.setConnectionId(connectionId);
//            // if not supported, it will be false
//            if (serverCheckResponse.isSupportAbilityNegotiation()) {
//                // mark
//                this.recAbilityContext.reset(grpcConn);
//                // promise null if no abilities receive
//                grpcConn.setAbilityTable(null);
//            }
//
//            //create stream request and bind connection event to this connection.
//            StreamObserver<Payload> payloadStreamObserver = bindRequestStream(biRequestStreamStub, grpcConn);
//
//            // stream observer to send response to server
//            grpcConn.setPayloadStreamObserver(payloadStreamObserver);
//            grpcConn.setGrpcFutureServiceStub(newChannelStubTemp);
//            grpcConn.setChannel(managedChannel);
//            //send a  setup request.
//            ConnectionSetupRequest conSetupRequest = new ConnectionSetupRequest();
//            conSetupRequest.setClientVersion(VersionUtils.getFullClientVersion());
//            conSetupRequest.setLabels(super.getLabels());
//            // set ability table
//            conSetupRequest
//                    .setAbilityTable(NacosAbilityManagerHolder.getInstance().getCurrentNodeAbilities(abilityMode()));
//            conSetupRequest.setTenant(super.getTenant());
//            //发起建立连接请求
//            grpcConn.sendRequest(conSetupRequest);
//            // wait for response
//            if (recAbilityContext.isNeedToSync()) {
//                // try to wait for notify response
//                recAbilityContext.await(this.clientConfig.capabilityNegotiationTimeout(), TimeUnit.MILLISECONDS);
//                // if no server abilities receiving, then reconnect
//                if (!recAbilityContext.check(grpcConn)) {
//                    return null;
//                }
//            } else {
//                // leave for adapting old version server
//                // registration is considered successful by default after 100ms
//                // wait to register connection setup
//                Thread.sleep(100L);
//            }
//            return grpcConn;
//        } catch (Exception e) {
//            LOGGER.error("[{}]Fail to connect to server!,error={}", GrpcClient.this.getName(), e);
//            // remove and notify
//            recAbilityContext.release(null);
//        }
//        return null;
//    }
//
//    @Override
//    public void shutdown() throws SparrowException {
//        super.shutdown();
////        if (grpcExecutor != null) {
////            LOGGER.info("Shutdown grpc executor " + grpcExecutor);
////            grpcExecutor.shutdown();
////        }
//    }
//
//    private Response serverCheck(String ip, int port, RequestGrpc.RequestFutureStub requestBlockingStub) {
//        try {
//            ServerCheckRequest serverCheckRequest = new ServerCheckRequest();
//            Payload grpcRequest = GrpcUtils.convert(serverCheckRequest);
//            ListenableFuture<Payload> responseFuture = requestBlockingStub.request(grpcRequest);
//            Payload response = responseFuture.get(clientConfig.serverCheckTimeOut(), TimeUnit.MILLISECONDS);
//            //receive connection unregister response here,not check response is success.
//            return (Response) GrpcUtils.parse(response);
//        } catch (Exception e) {
//            LoggerUtils.printIfErrorEnabled(LOGGER,
//                    "Server check fail, please check server {} ,port {} is available , error ={}", ip, port, e);
//            if (this.clientConfig != null && this.clientConfig.tlsConfig() != null && this.clientConfig.tlsConfig()
//                    .getEnableTls()) {
//                LoggerUtils.printIfErrorEnabled(LOGGER,
//                        "current client is require tls encrypted ,server must support tls ,please check");
//            }
//            return null;
//        }
//    }

}
