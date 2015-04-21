package com.jpm.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.external.Message;
import com.jpm.scheduler.retry.SyncRetryPolicy;
import com.jpm.scheduler.retry.Task;

public class RespondingTest {

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
    public void shouldProcessAllMessages() throws InterruptedException {

        sut = new ResourceScheduler(1, factory);
        Random generator = new Random();

        final int count = 5;
        for (int i = 0; i < count; i++) {
            sut.process(new ConcreteMessage("group1", String.valueOf(generator
                    .nextInt(3))));
        }

        retry.execute(new Task() {

            @Override
            public boolean isDone() {
                List<Message> channel1 = spy.get(0);
                if (channel1.isEmpty()) {
                    return false;
                }
                return channel1.size() == count;
            }
        });
    }

}
