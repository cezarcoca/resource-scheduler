package com.jpm.scheduler;

import java.util.List;
import java.util.Map;

import com.external.Gateway;
import com.external.Message;

public class AsyncGatewayFactory implements GatewayFactory {

    private Map<Integer, List<Message>> spy;
    private int channel;

    public AsyncGatewayFactory(Map<Integer, List<Message>> spy) {
        super();
        this.spy = spy;
        this.channel = 0;
    }

    @Override
    public Gateway getGateway() {
        return new AsyncSpyGateway(channel++, spy);
    }

}
