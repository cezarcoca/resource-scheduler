package com.jpm.scheduler.scaffolding;

import java.util.ArrayList;
import java.util.List;

import com.external.Gateway;
import com.jpm.scheduler.GatewayFactory;

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

    public List<SpyGateway> getGateways() {
        return gateways;
    }

}
