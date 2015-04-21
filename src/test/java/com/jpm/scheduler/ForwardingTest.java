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

        assertEquals(expected, factory.spy.getMessage());
    }

    @Test
    public void shouldSendMultipleMessagesInParallelIfMultipleResourcesAreAvailable() {

    }
}
