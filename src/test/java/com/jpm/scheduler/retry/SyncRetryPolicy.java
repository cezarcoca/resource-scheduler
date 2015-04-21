package com.jpm.scheduler.retry;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * @author ccoca
 *
 */
public class SyncRetryPolicy {

    private static final Logger LOGGER = Logger
            .getLogger(SyncRetryPolicy.class);

    private int timeInSeconds;
    private final int maxRetries;

    /**
     * Instantiates a new sync retry policy.
     *
     * @param timeInSeconds
     *            the time in seconds between retries
     * @param maxRetries
     *            the maximum number of retries retries
     */
    public SyncRetryPolicy(final int timeInSeconds, final int maxRetries) {
        super();
        this.timeInSeconds = timeInSeconds;
        this.maxRetries = maxRetries;
    }

    /**
     * Execute the task an throws error if the expectation is not fulfilled in
     * the configured amount of time.
     *
     * @param task
     *            the task that implements the expectation
     */
    public void execute(final Task task) {
        int numberOfAttempts = 1;
        try {
            do {
                LOGGER.info(String.format("Step #%d", numberOfAttempts));

                if (task.isDone()) {
                    LOGGER.info("Task done successfully.");
                    return;
                }
                TimeUnit.SECONDS.sleep(timeInSeconds);
            } while (numberOfAttempts++ < maxRetries);
        } catch (final InterruptedException e) {
            throw new IllegalStateException("Task was interrupted");
        }
        throw new IllegalStateException(
                "The maximum retry count has been exceed.");
    }
}
