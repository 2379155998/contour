package com.contour.service.impl;

import com.contour.component.V2rayComponent;
import com.contour.model.entity.VMessUser;
import com.contour.service.VMessService;
import org.springframework.stereotype.Service;

@Service
public class VMessServiceImpl implements VMessService {
    private final V2rayComponent v2rayComponent;

    public VMessServiceImpl(V2rayComponent v2rayComponent) {
        this.v2rayComponent = v2rayComponent;
    }

    @Override
    public void insertVMessUser(VMessUser vMessUser) {
//        Account.Builder accountBuilder = Account.newBuilder();
//        accountBuilder.setId(vMessUser.getUuid());
//        accountBuilder.setAlterId(vMessUser.getAlterId());
//
//        TypedMessage.Builder accountTypeMessageBuilder = typedMessageBuilder(Account.getDescriptor().getFullName(),
//                accountBuilder.build().toByteString());
//
//        com.v2ray.core.common.protocol.User.Builder userBuilder = com.v2ray.core.common.protocol.User.newBuilder();
//        userBuilder.setAccount(accountTypeMessageBuilder);
//        User user = vMessUser.getUser();
//        userBuilder.setLevel(user.getDataPlan().getLevel());
//        userBuilder.setEmail(user.getEmail());
//
//        AddUserOperation.Builder addUserOperationBuilder = AddUserOperation.newBuilder();
//        addUserOperationBuilder.setUser(userBuilder);
//
//        TypedMessage.Builder addUserOperationTypedMessageBuilder = typedMessageBuilder(AddUserOperation.getDescriptor().getFullName(),
//                addUserOperationBuilder.build().toByteString());
//
//        AlterInboundRequest.Builder alterInboundRequestBuilder = AlterInboundRequest.newBuilder();
//        alterInboundRequestBuilder.setTag("srv17329-vmess-ws-inbound");
//        alterInboundRequestBuilder.setOperation(addUserOperationTypedMessageBuilder);

//        AlterInboundResponse alterInboundResponse = handlerServiceBlockingStub.alterInbound(alterInboundRequestBuilder.build());
    }
}
