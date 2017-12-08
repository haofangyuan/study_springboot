package com.hfy.twoway.other.client;

import com.hfy.thrift.Message;

/**
 * Interface implemented by classes that want to be notified when
 * new messages are received.
 *
 * @author Joel Meyer
 */
public interface MessageListener {
    /**
     * Called when a new message is received.
     *
     * @param msg The message that was received.
     */
    public void messageReceived(Message msg);
}
