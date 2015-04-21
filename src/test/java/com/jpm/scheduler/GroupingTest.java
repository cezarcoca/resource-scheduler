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
public class GroupingTest {

    private static final String GROUP1 = "group1";
    private static final String GROUP2 = "group2";
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
    public void shouldTryToNotInterleaveMessagesWherePossible() {
        sut = new ResourceScheduler(2, factory);

        sut.process(new ConcreteMessage(GROUP1, String.valueOf(2)));
        sut.process(new ConcreteMessage(GROUP2, String.valueOf(1)));
        sut.process(new ConcreteMessage(GROUP2, String.valueOf(2)));
        sut.process(new ConcreteMessage(GROUP1, String.valueOf(1)));

        retry.execute(new Task() {

            @Override
            public boolean isDone() {
                List<Message> channel1 = spy.get(0);
                List<Message> channel2 = spy.get(1);
                if (channel1.size() < 2 || channel2.size() < 2) {
                    return false;
                }
                for (Message message : channel1) {
                    if (!((ConcreteMessage) message).getGroupId()
                            .equals(GROUP1)) {
                        return false;
                    }
                }
                for (Message message : channel2) {
                    if (!((ConcreteMessage) message).getGroupId()
                            .equals(GROUP2)) {
                        return false;
                    }
                }
                return true;
            }
        });

    }
}
