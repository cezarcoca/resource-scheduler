package com.jpm.scheduler.retry;

/**
 * The expectation abstraction.
 *
 * @author ccoca
 */
public interface Task {

    /**
     * Checks if the expectation is fulfilled.
     *
     * @return true, if the expectation is fulfilled
     */
    boolean isDone();
}
