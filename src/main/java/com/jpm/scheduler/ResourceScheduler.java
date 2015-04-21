package com.jpm.scheduler;

import static java.util.Objects.requireNonNull;

/**
 * @author ccoca
 *
 */
public class ResourceScheduler {

    private GatewayFactory factory;

    public ResourceScheduler(int noOfResources, GatewayFactory factory) {
        super();
        requireNonNull(factory, "Gateway Factory cannot be null.");
        this.factory = factory;
    }

    public void process(ConcreteMessage message) {
        factory.getGateway().send(message);
    }

}
