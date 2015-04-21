package com.jpm.scheduler;

import static java.util.Objects.requireNonNull;

/**
 * @author ccoca
 *
 */
public class ResourceScheduler {

    private GatewayFactory factory;
    private int noOfResources;

    public ResourceScheduler(int noOfResources, GatewayFactory factory) {

        requireNonNull(factory, "Gateway Factory cannot be null.");
        if (noOfResources < 1) {
            throw new IllegalArgumentException(
                    "No of resources should not be less than 1");
        }

        this.factory = factory;
        this.noOfResources = noOfResources;
    }

    public void process(ConcreteMessage message) {
        factory.getGateway().send(message);
    }

}
