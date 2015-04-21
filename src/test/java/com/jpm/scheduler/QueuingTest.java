package com.jpm.scheduler;

import static org.junit.Assert.assertEquals;

public class QueuingTest {

    private ResourceScheduler sut;

    public void shouldQueueTheMessagesWhenNoResourcesAreAvailable() {

        final SpyGatewayFactory factory = new SpyGatewayFactory();
        sut = new ResourceScheduler(1, factory);

        sut.process(new ConcreteMessage("groupId1", "payload1"));
        sut.process(new ConcreteMessage("groupId1", "payload2"));

        assertEquals(1, sut.getPendingMessages());
    }
}
