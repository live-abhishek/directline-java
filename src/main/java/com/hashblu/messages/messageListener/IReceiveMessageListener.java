package com.hashblu.messages.messageListener;

import com.hashblu.messages.HandOffGenericMessage;

/**
 * Created by abhisheks on 23-Oct-17.
 */
public interface IReceiveMessageListener {
    void processMessage(String msg);
    void processMessage(HandOffGenericMessage msg);
}
