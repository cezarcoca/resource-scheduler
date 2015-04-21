package com.jpm.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.external.Gateway;

public class SpyGatewayFactory implements GatewayFactory {

    List<SpyGateway> gateways;

    public SpyGatewayFactory() {
        gateways = new ArrayList<SpyGateway>();
    }

    @Override
    public Gateway getGateway() {
        SpyGateway spy = new SpyGateway();
        gateways.add(spy);
        return spy;
    }
}
