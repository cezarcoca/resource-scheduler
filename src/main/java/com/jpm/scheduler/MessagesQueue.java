package com.jpm.scheduler;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ccoca
 * 
 *         This class encapsulate a messages queue and is thread safe
 *
 */
public class MessagesQueue {

    private List<ConcreteMessage> queue;

    public MessagesQueue() {
        queue = new ArrayList<ConcreteMessage>();
    }

    /**
     * Add the message to the end of the queue.
     *
     * @param message
     *            the message
     */
    public synchronized void enqueue(ConcreteMessage message) {

        requireNonNull(message, "Message should not be null.");
        queue.add(message);
    }

    public synchronized ConcreteMessage dequeue(String filter) {
        if (queue.isEmpty()) {
            return null;
        }

        for (int i = 0; i < queue.size(); i++) {
            ConcreteMessage message = queue.get(i);
            if (message.getGroupId().equals(filter)) {
                return queue.remove(i);
            }
        }

        return queue.remove(0);
    }

    public synchronized int size() {
        return queue.size();
    }
}
