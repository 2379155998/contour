package com.contour.service.impl;

import com.contour.model.entity.Server;
import com.contour.service.ServerService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerServiceImplTest {
    private final Logger logger = LoggerFactory.getLogger(ServerServiceImplTest.class);
    @Autowired
    private ServerService serverService;

    @Test
    void insertServer() {
        Server server = serverService.insertServer(new Server(null, "server.codetour.com", 10085, null, null));
        logger.info(server.toString());
    }

    @Test
    void removeServer() {
        Server server = serverService.getServerByIp("server.codetour.com");
        serverService.removeServer(server);
    }

    @Test
    void listV2rayServer() {
        serverService.listV2rayServer().forEach(server -> logger.info(server.toString()));
    }

    @Test
    void getServerByIp() {
        Server server = serverService.getServerByIp("server.codetour.tk");
        logger.info(server.toString());
    }
}