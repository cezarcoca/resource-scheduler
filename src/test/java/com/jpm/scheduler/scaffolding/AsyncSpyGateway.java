package com.jpm.scheduler.scaffolding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.external.Gateway;
import com.external.Message;
import com.jpm.scheduler.ConcreteMessage;

public class AsyncSpyGateway implements Gateway {

    private static final Logger LOGGER = Logger
            .getLogger(AsyncSpyGateway.class);
    private static ExecutorService service = Executors.newFixedThreadPool(3);

    private Integer channel;
    private Map<Integer, List<Message>> spy;

    public AsyncSpyGateway(int channel, Map<Integer, List<Message>> spy) {
        this.channel = Integer.valueOf(channel);
        this.spy = spy;
        this.spy.put(this.channel, new ArrayList<Message>());
    }

    @Override
    public void send(final Message message) {
        service.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(Integer
                            .valueOf(((ConcreteMessage) message).getPayload()));
                    message.completed();
                    List<Message> messages = spy.get(channel);
                    messages.add(message);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });
    }
}
