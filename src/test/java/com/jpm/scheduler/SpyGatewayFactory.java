package com.jpm.scheduler;

import com.external.Gateway;

public class SpyGatewayFactory implements GatewayFactory {

    SpyGateway spy;

    public SpyGatewayFactory() {
        this.spy = new SpyGateway();
    }

    @Override
    public Gateway getGateway() {
        return spy;
    }
}
