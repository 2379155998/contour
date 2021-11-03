package com.contour.service.impl;

import com.contour.component.V2rayComponent;
import com.contour.model.dto.V2rayOperateClient;
import com.contour.model.entity.Server;
import com.contour.model.entity.User;
import com.contour.service.ServerService;
import com.contour.service.V2rayService;
import com.google.protobuf.Any;
import com.v2ray.core.app.proxyman.command.*;
import com.v2ray.core.app.stats.command.QueryStatsRequest;
import com.v2ray.core.app.stats.command.QueryStatsResponse;
import com.v2ray.core.proxy.vmess.Account;
import org.springframework.stereotype.Service;

@Service
public class V2rayServiceImpl implements V2rayService {
    private final ServerService serverService;
    private final V2rayComponent v2rayComponent;

    public V2rayServiceImpl(ServerService serverService, V2rayComponent v2rayComponent) {
        this.serverService = serverService;
        this.v2rayComponent = v2rayComponent;
    }

    @Override
    public void insertUser(User user) {
        Server server = serverService.getServerByIp("server.codetour.ml");
        System.out.println(server);
        V2rayOperateClient v2RayOperateClient = v2rayComponent.getV2rayClient(server);
        HandlerServiceGrpc.HandlerServiceBlockingStub handlerServiceBlockingStub = v2RayOperateClient.getHandlerServiceBlockingStub();

        insertVmessUser(handlerServiceBlockingStub,null);
        removeVmessUser(handlerServiceBlockingStub);

        QueryStatsResponse queryStatsResponse = v2RayOperateClient.getStatsServiceBlockingStub().queryStats(QueryStatsRequest.newBuilder()
                .setPattern("")
                .setReset(false)
                .build());
        queryStatsResponse.getStatList().forEach(stat -> {
            System.out.println(stat.getName());
            System.out.println(stat.getValue());
        });
    }

    @Override
    public void insertVmessUser(HandlerServiceGrpc.HandlerServiceBlockingStub handlerServiceBlockingStub, User user) {
//        VMessUser vmessUser = user.getVmessUser();

        Account.Builder accountBuilder = Account.newBuilder();
        accountBuilder.setId("712f2f3b-de88-4869-9427-5368ed1f785a");
        accountBuilder.setAlterId(64);

        com.v2ray.core.common.protocol.User.Builder userBuilder = com.v2ray.core.common.protocol.User.newBuilder();
        userBuilder.setAccount(Any.newBuilder()
                        .setTypeUrl(Account.getDescriptor().getFullName())
                        .setValue(accountBuilder.build().toByteString())
                .build());
        userBuilder.setLevel(0);
        userBuilder.setEmail("2379155998@qq.com");

        AddUserOperation.Builder addUserOperationBuilder = AddUserOperation.newBuilder();
        addUserOperationBuilder.setUser(userBuilder);

        AlterInboundRequest.Builder alterInboundRequestBuilder = AlterInboundRequest.newBuilder();
        alterInboundRequestBuilder.setTag("srv17329-vmess-ws-inbound");
        alterInboundRequestBuilder.setOperation(Any.newBuilder()
                        .setTypeUrl(AddUserOperation.getDescriptor().getFullName())
                        .setValue(addUserOperationBuilder.build().toByteString())
                .build());
        AlterInboundResponse alterInboundResponse = handlerServiceBlockingStub.alterInbound(alterInboundRequestBuilder.build());
    }

    @Override
    public void removeVmessUser(HandlerServiceGrpc.HandlerServiceBlockingStub handlerServiceBlockingStub) {
        RemoveUserOperation.Builder removeUserOperationBuilder = RemoveUserOperation.newBuilder();
        removeUserOperationBuilder.setEmail("2379155998@qq.com");

//        TypedMessage.Builder removeUserOperationTypedMessageBuilder = typedMessageBuilder(RemoveUserOperation.getDescriptor().getFullName(),
//                removeUserOperationBuilder.build().toByteString());

        AlterInboundRequest.Builder alterInboundRequestBuilder = AlterInboundRequest.newBuilder();
        alterInboundRequestBuilder.setTag("srv17329-vmess-ws-inbound");
//        alterInboundRequestBuilder.setOperation(removeUserOperationTypedMessageBuilder);
        alterInboundRequestBuilder.setOperation(Any.newBuilder()
                        .setTypeUrl(RemoveUserOperation.getDescriptor().getFullName())
                        .setValue(removeUserOperationBuilder.build().toByteString())
                .build());
        AlterInboundResponse alterInboundResponse = handlerServiceBlockingStub.alterInbound(alterInboundRequestBuilder.build());
    }
}
