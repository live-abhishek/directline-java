package com.hashblu;

import com.hashblu.agents.HandOffGenericMessage;

/**
 * Created by abhisheks on 23-Oct-17.
 */
public interface IReceiveMessageListener {
    void processMessage(String msg);
    void processMessage(HandOffGenericMessage msg);
}
