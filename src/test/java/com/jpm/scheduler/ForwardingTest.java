package com.jpm.scheduler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ForwardingTest {

    private ResourceScheduler sut;

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfGatewayIsNull() {
        sut = new ResourceScheduler(null);
    }

    @Test
    public void shouldSendTheMessageToTheGatewayWhenOneResourceIsAvailable() {

        SpyGateway spy = new SpyGateway();
        sut = new ResourceScheduler(spy);

        ConcreteMessage expected = new ConcreteMessage("groupId", "payload");
        sut.process(expected);

        assertEquals(expected, spy.getMessage());
    }
}
