package com.external;

public interface Gateway {

    /**
     * Send the message to the very expensive external resource.
     *
     * @param msg
     *            the message
     */
    void send(Message message);
}
