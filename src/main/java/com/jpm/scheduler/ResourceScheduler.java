package com.jpm.scheduler;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ccoca
 *
 */
public class ResourceScheduler {

    private List<Resource> resources;
    private ExecutorService executorService;
    private MessagesQueue queue;

    public ResourceScheduler(int noOfResources, GatewayFactory factory) {

        requireNonNull(factory, "Gateway Factory cannot be null.");
        if (noOfResources < 1) {
            throw new IllegalArgumentException(
                    "No of resources should not be less than 1");
        }

        executorService = Executors.newFixedThreadPool(noOfResources + 1);
        queue = new MessagesQueue();
        resources = new ArrayList<Resource>();
        for (int i = 0; i < noOfResources; i++) {
            resources.add(new Resource(factory, queue));
        }
    }

    public void process(final ConcreteMessage message) {
        queue.enqueue(message);
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                for (Resource resource : resources) {
                    if (resource.accept()) {
                        ConcreteMessage first = queue.dequeue(null);
                        resource.send(first);
                        return;
                    }
                }
            }
        });
    }

    public int getPendingMessages() {
        return queue.size();
    }

}
