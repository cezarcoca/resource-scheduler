/**
 * 
 */
package com.jpm.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.external.Message;
import com.jpm.scheduler.retry.SyncRetryPolicy;
import com.jpm.scheduler.retry.Task;
import com.jpm.scheduler.scaffolding.AsyncGatewayFactory;

/**
 * @author ccoca
 *
 */
public class PrioritiesTest {

    private ResourceScheduler sut;
    private Map<Integer, List<Message>> spy;
    private GatewayFactory factory;
    private SyncRetryPolicy retry;

    @Before
    public void setUp() {
        spy = new HashMap<Integer, List<Message>>();
        factory = new AsyncGatewayFactory(spy);
        retry = new SyncRetryPolicy(1, 30);
    }

    @Test
    public void shouldPrioritiseMessages() throws InterruptedException {

        sut = new ResourceScheduler(1, factory);

        String sameGroupId = "group2";

        final ConcreteMessage message1 = new ConcreteMessage(sameGroupId,
                String.valueOf(1));
        final ConcreteMessage message2 = new ConcreteMessage("group1",
                String.valueOf(1));
        final ConcreteMessage message3 = new ConcreteMessage(sameGroupId,
                String.valueOf(1));
        final ConcreteMessage message4 = new ConcreteMessage("group3",
                String.valueOf(1));

        sut.process(message1);
        sut.process(message2);
        sut.process(message3);
        sut.process(message4);

        retry.execute(new Task() {

            @Override
            public boolean isDone() {
                List<Message> completedMessages = spy.get(0);
                if (completedMessages == null || completedMessages.size() < 4) {
                    return false;
                }
                return completedMessages.get(0).equals(message1)
                        && completedMessages.get(1).equals(message3)
                        && completedMessages.get(2).equals(message2)
                        && completedMessages.get(3).equals(message4);
            }
        });
    }
}
