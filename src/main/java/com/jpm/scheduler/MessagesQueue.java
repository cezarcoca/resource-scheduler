package com.jpm.scheduler;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author ccoca
 * 
 *         This class encapsulate a messages queue and is thread safe
 *
 */
public class MessagesQueue {

    private List<ConcreteMessage> queue;
    private Set<String> cancelledGroups;

    public MessagesQueue() {
        queue = new LinkedList<ConcreteMessage>();
        cancelledGroups = new HashSet<String>();
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

            if (cancelledGroups.contains(message.getGroupId())) {
                queue.remove(i--);
                continue;
            }

            if (message.getGroupId().equals(filter)) {
                return queue.remove(i);
            }
        }

        if (queue.isEmpty()) {
            return null;
        }

        return queue.remove(0);
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized void addCancelledGroup(String groupId) {
        cancelledGroups.add(groupId);
    }
}
