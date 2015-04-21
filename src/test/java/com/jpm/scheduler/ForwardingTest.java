package com.jpm.scheduler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ForwardingTest {

    private ResourceScheduler sut;

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfGatewayFactoryIsNull() {
        sut = new ResourceScheduler(1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoOfResourcesIsLessThanOne() {
        sut = new ResourceScheduler(0, new SpyGatewayFactory());
    }

    @Test
    public void shouldSendTheMessageToTheGatewayWhenOneResourceIsAvailable() {

        SpyGatewayFactory factory = new SpyGatewayFactory();
        sut = new ResourceScheduler(1, factory);

        ConcreteMessage expected = new ConcreteMessage("groupId", "payload");
        sut.process(expected);

        assertEquals(expected, factory.gateways.get(0).getMessage());
    }

    @Test
    public void shouldSendMultipleMessagesInParallelIfMultipleResourcesAreAvailable() {

        SpyGatewayFactory factory = new SpyGatewayFactory();
        sut = new ResourceScheduler(2, factory);

        ConcreteMessage message1 = new ConcreteMessage("groupId1", "payload1");
        ConcreteMessage message2 = new ConcreteMessage("groupId2", "payload2");

        sut.process(message1);
        sut.process(message2);

        assertEquals(message1, factory.gateways.get(0).getMessage());
        assertEquals(message2, factory.gateways.get(1).getMessage());
    }
}
