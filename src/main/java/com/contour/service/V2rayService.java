package com.contour.service;

import com.contour.model.entity.User;
import com.v2ray.core.app.proxyman.command.HandlerServiceGrpc;

public interface V2rayService {
    void insertUser(User user);

    void insertVmessUser(HandlerServiceGrpc.HandlerServiceBlockingStub handlerServiceBlockingStub, User user);

    void removeVmessUser(HandlerServiceGrpc.HandlerServiceBlockingStub handlerServiceBlockingStub);
}
