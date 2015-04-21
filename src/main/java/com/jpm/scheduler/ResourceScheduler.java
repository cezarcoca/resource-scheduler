package com.jpm.scheduler;

import static java.util.Objects.requireNonNull;

import com.external.Gateway;

/**
 * @author ccoca
 *
 */
public class ResourceScheduler {

    private Gateway gateway;

    public ResourceScheduler(Gateway gateway) {
        super();
        requireNonNull(gateway, "Gateway cannot be null.");
        this.gateway = gateway;
    }

    public void process(ConcreteMessage message) {
        gateway.send(message);
    }

}
