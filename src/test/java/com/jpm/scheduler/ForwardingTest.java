package com.jpm.scheduler;

import org.junit.Before;
import org.junit.Test;

import com.jpm.scheduler.retry.SyncRetryPolicy;
import com.jpm.scheduler.retry.Task;
import com.jpm.scheduler.scaffolding.SpyGatewayFactory;

public class ForwardingTest {

    private ResourceScheduler sut;
    private SyncRetryPolicy retry;

    @Before
    public void setUp() {
        retry = new SyncRetryPolicy(1, 30);
    }

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

        final SpyGatewayFactory factory = new SpyGatewayFactory();
        sut = new ResourceScheduler(1, factory);

        final ConcreteMessage expected = new ConcreteMessage("groupId",
                "payload");
        sut.process(expected);

        retry.execute(new Task() {

            @Override
            public boolean isDone() {
                return expected.equals(factory.getGateways().get(0)
                        .getMessage());
            }
        });

    }

    @Test
    public void shouldSendMultipleMessagesInParallelIfMultipleResourcesAreAvailable() {

        final SpyGatewayFactory factory = new SpyGatewayFactory();
        sut = new ResourceScheduler(2, factory);

        final ConcreteMessage message1 = new ConcreteMessage("groupId1",
                "payload1");
        final ConcreteMessage message2 = new ConcreteMessage("groupId2",
                "payload2");

        sut.process(message1);
        sut.process(message2);

        retry.execute(new Task() {

            @Override
            public boolean isDone() {
                return message1.equals(factory.getGateways().get(0)
                        .getMessage());
            }
        });

        retry.execute(new Task() {

            @Override
            public boolean isDone() {
                return message2.equals(factory.getGateways().get(1)
                        .getMessage());
            }
        });
    }
}
