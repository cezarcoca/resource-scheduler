package com.jpm.scheduler;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jpm.scheduler.prioritisation.PrioritisationByGroupName;
import com.jpm.scheduler.prioritisation.PrioritisationFilter;

/**
 * @author ccoca
 * 
 *         This class encapsulate a messages queue and is thread safe
 *
 */
public class MessagesQueue {

    private static final Logger LOGGER = Logger.getLogger(MessagesQueue.class);
    public static final String PRIORITISATION_STRATEGY = "com.jpm.scheduler.prioritisation.strategy";

    private List<ConcreteMessage> queue;
    private Set<String> cancelledGroups;
    private PrioritisationFilter prioritisationFilter;

    public MessagesQueue() {
        queue = new LinkedList<ConcreteMessage>();
        cancelledGroups = new HashSet<String>();
        prioritisationFilter = getPrioritisationFilter();
    }

    /**
     * Factory method - gets the prioritization strategy.
     *
     * @return the prioritization filter
     */
    private PrioritisationFilter getPrioritisationFilter() {
        String strategyClassName = System.getProperty(PRIORITISATION_STRATEGY);

        if (strategyClassName == null) {
            LOGGER.info(String.format(
                    "Default proritisation strategy was set: %s",
                    PrioritisationByGroupName.class.getName()));
            return new PrioritisationByGroupName();
        }

        try {
            return (PrioritisationFilter) Class.forName(strategyClassName)
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException
                | ClassNotFoundException e) {
            LOGGER.info(String
                    .format("Class not found: %s. Default proritisation strategy was set: %s",
                            strategyClassName,
                            PrioritisationByGroupName.class.getName()));
            return new PrioritisationByGroupName();
        }
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

            if (prioritisationFilter.match(message, filter)) {
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
