package com.jpm.scheduler;

import com.external.Gateway;
import com.external.Message;

public class SpyGateway implements Gateway {

    private Message message;

    @Override
    public void send(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
