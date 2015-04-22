package com.jpm.scheduler.prioritisation;

import com.jpm.scheduler.ConcreteMessage;

/**
 * @author ccoca
 *
 */
public interface PrioritisationFilter {

    /**
     * Check if the given message match the condition implemented by the
     * concrete strategy.
     *
     * @param message
     *            the message
     * @param filter
     *            the filter
     * @return true, if match
     */
    boolean match(ConcreteMessage message, String filter);
}
