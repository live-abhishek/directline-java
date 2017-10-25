package com.hashblu.agents;

import com.hashblu.messages.HandOffGenericMessage;

import java.util.List;

/**
 * Created by abhisheks on 16-Oct-17.
 */
public class SalesforceAgentClient implements IAgentClient {

    @Override
    public void startChat() {

    }

    @Override
    public List<HandOffGenericMessage> receiveChat() {
        return null;
    }

    @Override
    public void sendChat(String msg) {

    }

    @Override
    public void closeChat() {

    }
}
