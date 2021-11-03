package com.contour.model.dto;

import com.v2ray.core.app.log.command.LoggerServiceGrpc;
import com.v2ray.core.app.proxyman.command.HandlerServiceGrpc;
import com.v2ray.core.app.router.command.RoutingServiceGrpc;
import com.v2ray.core.app.stats.command.StatsServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class V2rayOperateClient {
    private ManagedChannel managedChannel;
    private LoggerServiceGrpc.LoggerServiceBlockingStub loggerServiceBlockingStub;
    private HandlerServiceGrpc.HandlerServiceBlockingStub handlerServiceBlockingStub;
    private RoutingServiceGrpc.RoutingServiceBlockingStub routingServiceBlockingStub;
    private StatsServiceGrpc.StatsServiceBlockingStub statsServiceBlockingStub;

    public boolean shutdown() {
        managedChannel.shutdownNow();
        return managedChannel.isShutdown();
    }
}
