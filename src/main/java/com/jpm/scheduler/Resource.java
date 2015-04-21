package com.jpm.scheduler;

import com.external.Gateway;

/**
 * @author ccoca
 *
 *         This class is a wrapper for external resources. Its main
 *         responsibility is to act as a Proxy and to redirect the message to
 *         the real resource only if this is idle.
 */
public class Resource {

    private Gateway gateway;
    private ConcreteMessage messageUnderProcess;

    public Resource(GatewayFactory factory) {
        gateway = factory.getGateway();
        messageUnderProcess = null;
    }

    public void send(ConcreteMessage message) {
        messageUnderProcess = message;
        gateway.send(message);
    }

    /**
     * Check if the wrapped resource is idle.
     * 
     * @return true if wrapped resource is idle and false else
     */
    public boolean accept() {
        return messageUnderProcess == null;
    }
}
