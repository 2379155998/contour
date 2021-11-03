package com.contour.component;

import cn.hutool.core.util.ObjectUtil;
import com.contour.model.dto.V2rayOperateClient;
import com.contour.model.entity.Server;
import com.contour.repository.ServerRepository;
import com.v2ray.core.app.log.command.LoggerServiceGrpc;
import com.v2ray.core.app.proxyman.command.HandlerServiceGrpc;
import com.v2ray.core.app.router.command.RoutingServiceGrpc;
import com.v2ray.core.app.stats.command.StatsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class V2rayComponent {
    private final ServerRepository serverRepository;

    private final ConcurrentHashMap<Server, V2rayOperateClient> concurrentHashMap = new ConcurrentHashMap<>();

    public V2rayComponent(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void loadV2rayClients() {
        serverRepository.findAll().forEach(this::getV2rayClient);
    }

    public V2rayOperateClient putV2rayOperateClient(Server server) {
        V2rayOperateClient v2rayOperateClient = createV2rayClient(server);
        return concurrentHashMap.put(server, v2rayOperateClient);
    }

    public void delV2rayOperateClient(Server server) {
        V2rayOperateClient v2rayOperateClient = concurrentHashMap.get(server);
        if (v2rayOperateClient.shutdown()) {
            concurrentHashMap.remove(server);
        }
    }

    public V2rayOperateClient getV2rayClient(Server server) {
        V2rayOperateClient v2rayOperateClient = concurrentHashMap.get(server);
        if (ObjectUtil.isNull(v2rayOperateClient)) {
            return putV2rayOperateClient(server);
        }
        return concurrentHashMap.get(server);
    }

    private V2rayOperateClient createV2rayClient(Server server) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(server.getIp(), server.getPort()).usePlaintext().build();

        LoggerServiceGrpc.LoggerServiceBlockingStub loggerServiceBlockingStub = LoggerServiceGrpc.newBlockingStub(managedChannel);
        HandlerServiceGrpc.HandlerServiceBlockingStub handlerServiceBlockingStub = HandlerServiceGrpc.newBlockingStub(managedChannel);
        RoutingServiceGrpc.RoutingServiceBlockingStub routingServiceBlockingStub = RoutingServiceGrpc.newBlockingStub(managedChannel);
        StatsServiceGrpc.StatsServiceBlockingStub statsServiceBlockingStub = StatsServiceGrpc.newBlockingStub(managedChannel);

        return new V2rayOperateClient(managedChannel,loggerServiceBlockingStub, handlerServiceBlockingStub, routingServiceBlockingStub, statsServiceBlockingStub);
    }
}
