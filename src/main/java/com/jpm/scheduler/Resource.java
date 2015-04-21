package com.jpm.scheduler;

import org.apache.log4j.Logger;

import com.external.Gateway;

/**
 * @author ccoca
 *
 *         This class is a wrapper for external resources. Its main
 *         responsibility is to act as a Proxy and to redirect the message to
 *         the real resource only if this is idle.
 */
public class Resource {

    private static final Logger LOGGER = Logger.getLogger(Resource.class);

    private Gateway gateway;
    private ConcreteMessage messageUnderProcess;
    private MessagesQueue queue;

    public Resource(GatewayFactory factory, MessagesQueue queue) {
        this.gateway = factory.getGateway();
        this.messageUnderProcess = null;
        this.queue = queue;
    }

    public void send(ConcreteMessage message) {
        messageUnderProcess = message;
        messageUnderProcess.setResource(this);
        LOGGER.info("Send message: " + messageUnderProcess);
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

    /**
     * Notify completion of message processing.
     */
    void notifyCompletion() {
        LOGGER.debug("Message processed successfully: " + messageUnderProcess);
        if (hasMessageToProcess()) {
            send(messageUnderProcess);
        }
    }

    /**
     * Checks for next message to process. If message is found, the message is
     * removed from the queue and is set as current message under process.
     *
     * @return true, if the are pending messages
     */
    private boolean hasMessageToProcess() {
        synchronized (queue) {
            messageUnderProcess = queue.dequeue(null);
            if (messageUnderProcess == null) {
                LOGGER.debug("No pending messages.");
                return false;
            }
            messageUnderProcess.setResource(this);
        }

        return true;
    }
}
