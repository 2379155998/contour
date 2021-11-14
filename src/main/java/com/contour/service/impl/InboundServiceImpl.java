package com.contour.service.impl;

import com.contour.component.V2rayComponent;
import com.contour.model.entity.Header;
import com.contour.model.entity.Inbound;
import com.contour.service.InboundService;
import com.contour.utils.AnyUtil;
import com.v2ray.core.InboundHandlerConfig;
import com.v2ray.core.app.proxyman.AllocationStrategy;
import com.v2ray.core.app.proxyman.ReceiverConfig;
import com.v2ray.core.app.proxyman.SniffingConfig;
import com.v2ray.core.app.proxyman.command.HandlerServiceGrpc;
import com.v2ray.core.common.net.IPOrDomain;
import com.v2ray.core.common.net.PortRange;
import com.v2ray.core.proxy.vmess.inbound.Config;
import com.v2ray.core.proxy.vmess.inbound.DefaultConfig;
import com.v2ray.core.transport.internet.StreamConfig;
import com.v2ray.core.transport.internet.TransportConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InboundServiceImpl implements InboundService {
    private final V2rayComponent v2rayComponent;

    public InboundServiceImpl(V2rayComponent v2rayComponent) {
        this.v2rayComponent = v2rayComponent;
    }

    @Override
    public void insertInbound(Inbound inbound) {
        HandlerServiceGrpc.HandlerServiceBlockingStub handlerServiceBlockingStub = v2rayComponent.getV2rayClient(inbound.getServer())
                .getHandlerServiceBlockingStub();

        InboundHandlerConfig.Builder inboundHandlerConfigBuilder = InboundHandlerConfig.newBuilder();
        inboundHandlerConfigBuilder.setTag(inbound.getTag());

        Config.Builder vMessInboundConfigBuilder = Config.newBuilder();

        DefaultConfig.Builder defaultConfigBuilder = DefaultConfig.newBuilder();
        defaultConfigBuilder.setLevel(inbound.getLevel());
        defaultConfigBuilder.setAlterId(inbound.getAlterId());

        vMessInboundConfigBuilder.setDefault(defaultConfigBuilder);
        vMessInboundConfigBuilder.setSecureEncryptionOnly(inbound.isSecureEncryptionOnly());
        inboundHandlerConfigBuilder.setProxySettings(AnyUtil.anyBuilder(Config.getDescriptor().getFullName(),vMessInboundConfigBuilder.build().toByteString()));

        ReceiverConfig.Builder receiverConfigBuilder = ReceiverConfig.newBuilder();
        IPOrDomain.Builder iPOrDomainBuilder = IPOrDomain.newBuilder();
        iPOrDomainBuilder.setDomain(inbound.getServer().getIp());
        receiverConfigBuilder.setListen(iPOrDomainBuilder);

        PortRange.Builder portRangeBuilder = PortRange.newBuilder();
        portRangeBuilder.setFrom(inbound.getServer().getPort());
        portRangeBuilder.setTo(inbound.getServer().getPort());
        receiverConfigBuilder.setPortRange(portRangeBuilder);

        StreamConfig.Builder streamConfigBuilder = StreamConfig.newBuilder();
        streamConfigBuilder.setProtocolName("ws");
        streamConfigBuilder.setSecurityType("none");

        TransportConfig.Builder transportConfigBuilder = TransportConfig.newBuilder();
        transportConfigBuilder.setProtocolName("ws");

        com.v2ray.core.transport.internet.websocket.Config.Builder websocketConfigBuilder = com.v2ray.core.transport.internet.websocket.Config.newBuilder();
        websocketConfigBuilder.setAcceptProxyProtocol(inbound.isAcceptProxyProtocol());
        websocketConfigBuilder.setPath(inbound.getPath());
        List<Header> headers = inbound.getHeaders();
        for (int i = 0; i < headers.size(); i++) {
            com.v2ray.core.transport.internet.websocket.Header.Builder headerBuilder = com.v2ray.core.transport.internet.websocket.Header.newBuilder();
            headerBuilder.setKey(headers.get(i).getKey());
            headerBuilder.setValue(headers.get(i).getValue());
            websocketConfigBuilder.setHeader(i,headerBuilder);
        }
        websocketConfigBuilder.setMaxEarlyData(inbound.getMaxEarlyData());
        websocketConfigBuilder.setUseBrowserForwarding(inbound.isUseBrowserForwarding());
        websocketConfigBuilder.setEarlyDataHeaderName(inbound.getEarlyDataHeaderName());

        transportConfigBuilder.setSettings(AnyUtil.anyBuilder(com.v2ray.core.transport.internet.websocket.Config.getDescriptor().getFullName(), websocketConfigBuilder.build().toByteString()));
        streamConfigBuilder.setTransportSettings(0,transportConfigBuilder);
        //streamConfigBuilder.setSocketSettings();
        receiverConfigBuilder.setStreamSettings(streamConfigBuilder);

        SniffingConfig.Builder sniffingConfigBuilder = SniffingConfig.newBuilder();
//        sniffingConfigBuilder.setEnabled()
        receiverConfigBuilder.setSniffingSettings(sniffingConfigBuilder);

        AllocationStrategy.Builder allocationStrategyBuilder = AllocationStrategy.newBuilder();
//        allocationStrategyBuilder.setType()
        receiverConfigBuilder.setAllocationStrategy(allocationStrategyBuilder);
        inboundHandlerConfigBuilder.setReceiverSettings(AnyUtil.anyBuilder(ReceiverConfig.getDescriptor().getFullName(), receiverConfigBuilder.build().toByteString()));
    }
}
