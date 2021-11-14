package com.contour.service.impl;

import com.contour.component.V2rayComponent;
import com.contour.model.dto.V2rayOperateClient;
import com.contour.model.entity.User;
import com.contour.repository.UserRepository;
import com.contour.service.UserService;
import com.contour.utils.AnyUtil;
import com.google.protobuf.Any;
import com.v2ray.core.app.proxyman.command.AddUserOperation;
import com.v2ray.core.app.proxyman.command.AlterInboundRequest;
import com.v2ray.core.app.proxyman.command.AlterInboundResponse;
import com.v2ray.core.app.proxyman.command.HandlerServiceGrpc;
import com.v2ray.core.proxy.vmess.Account;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final V2rayComponent v2rayComponent;

    public UserServiceImpl(UserRepository userRepository, V2rayComponent v2rayComponent) {
        this.userRepository = userRepository;
        this.v2rayComponent = v2rayComponent;
    }

    @Override
    public User insertUser(User user) {
        user.getDataPlan().getInbounds().forEach(inbound -> {
            Account.Builder accountBuilder = Account.newBuilder();
            accountBuilder.setId(user.getUuid());
            accountBuilder.setAlterId(user.getAlterId());

            Any.Builder AccountAnyBuilder = AnyUtil.anyBuilder(Account.getDescriptor().getFullName(), accountBuilder.build().toByteString());

            com.v2ray.core.common.protocol.User.Builder userBuilder = com.v2ray.core.common.protocol.User.newBuilder();
            userBuilder.setAccount(AccountAnyBuilder);
            userBuilder.setEmail(user.getEmail());
            userBuilder.setLevel(user.getDataPlan().getLevel());

            AddUserOperation.Builder addUserOperationBuilder = AddUserOperation.newBuilder();
            addUserOperationBuilder.setUser(userBuilder);

            Any.Builder addUserOperationAnyBuilder = AnyUtil.anyBuilder(AddUserOperation.getDescriptor().getFullName(), addUserOperationBuilder.build().toByteString());

            AlterInboundRequest.Builder alterInboundRequestBuilder = AlterInboundRequest.newBuilder();
            alterInboundRequestBuilder.setTag(inbound.getTag());
            alterInboundRequestBuilder.setOperation(addUserOperationAnyBuilder);

            V2rayOperateClient v2rayClient = v2rayComponent.getV2rayClient(inbound.getServer());
            HandlerServiceGrpc.HandlerServiceBlockingStub handlerServiceBlockingStub = v2rayClient.getHandlerServiceBlockingStub();
            AlterInboundResponse alterInboundResponse = handlerServiceBlockingStub.alterInbound(alterInboundRequestBuilder.build());
        });
        return userRepository.save(user);
    }
}
