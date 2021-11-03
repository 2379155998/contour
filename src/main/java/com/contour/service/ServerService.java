package com.contour.service;

import com.contour.model.entity.Server;

import java.util.List;

public interface ServerService {
    Server insertServer(Server server);
    void removeServer(Server server);
    void removeServerById(Long id);
    List<Server> listV2rayServer();
    Server getServerByIp(String ip);
}
