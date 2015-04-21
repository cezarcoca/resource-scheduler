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

    public ResourceScheduler(int noOfResources, GatewayFactory factory) {

        requireNonNull(factory, "Gateway Factory cannot be null.");
        if (noOfResources < 1) {
            throw new IllegalArgumentException(
                    "No of resources should not be less than 1");
        }

        executorService = Executors.newFixedThreadPool(noOfResources + 1);
        resources = new ArrayList<Resource>();
        for (int i = 0; i < noOfResources; i++) {
            resources.add(new Resource(factory));
        }
    }

    public void process(final ConcreteMessage message) {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                for (Resource resource : resources) {
                    if (resource.accept()) {
                        resource.send(message);
                        return;
                    }
                }
            }
        });
    }

}
