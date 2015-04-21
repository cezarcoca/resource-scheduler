package com.jpm.scheduler;

import com.external.Message;

/**
 * @author ccoca
 *
 *         The concrete implementation of {@link Message} interface.
 */
public class ConcreteMessage implements Message {

    private String groupId;
    private String payload;
    private Resource resource;

    public ConcreteMessage(String groupId, String payload) {
        super();
        this.groupId = groupId;
        this.payload = payload;
    }

    @Override
    public void completed() {
        resource.notifyCompletion();
    }

    public String getGroupId() {
        return groupId;
    }

    public String getPayload() {
        return payload;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
