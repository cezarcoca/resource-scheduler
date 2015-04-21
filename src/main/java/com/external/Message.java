package com.external;

/**
 * @author ccoca
 *
 */
public interface Message {

    /**
     * The callback method invoked by the external resource, after message is
     * processed.
     */
    void completed();
}
