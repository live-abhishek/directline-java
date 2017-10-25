package com.hashblu.messages.messageListener;

import com.hashblu.messages.HandOffGenericMessage;
import com.hashblu.messages.queue.IMessageQueue;

/**
 * Created by abhisheks on 25-Oct-17.
 */
public class DumperMessageListener implements IReceiveMessageListener {
    IMessageQueue queue;
    public DumperMessageListener(IMessageQueue msgQueue){
        queue = msgQueue;
    }

    @Override
    public void processMessage(String msg) {
        throw new RuntimeException("Not Implemented yet!");
    }

    @Override
    public void processMessage(HandOffGenericMessage msg) {
        queue.pushMsg(msg);
    }
}
