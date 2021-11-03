package com.contour.service.impl;

import com.contour.component.V2rayComponent;
import com.contour.model.entity.Server;
import com.contour.repository.ServerRepository;
import com.contour.service.ServerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {
    private final V2rayComponent v2rayComponent;
    private final ServerRepository serverRepository;

    public ServerServiceImpl(ServerRepository serverRepository, V2rayComponent v2rayComponent) {
        this.serverRepository = serverRepository;
        this.v2rayComponent = v2rayComponent;
    }

    @Override
    public Server insertServer(Server server) {
        v2rayComponent.putV2rayOperateClient(server);
        return serverRepository.save(server);
    }

    @Override
    public void removeServer(Server server) {
        v2rayComponent.delV2rayOperateClient(server);
        serverRepository.delete(server);
    }

    @Override
    public void removeServerById(Long id) {
        serverRepository.deleteById(id);
    }

    @Override
    public List<Server> listV2rayServer() {
        return serverRepository.findAll();
    }

    @Override
    public Server getServerByIp(String ip) {
        return serverRepository.findServerByIp(ip);
    }
}
