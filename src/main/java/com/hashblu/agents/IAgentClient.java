package com.hashblu.agents;

import com.hashblu.messages.HandOffGenericMessage;
import io.swagger.client.ApiException;

import java.util.List;

/**
 * Created by abhisheks on 13-Oct-17.
 */
public interface IAgentClient {
    void startChat() throws ApiException;
    List<HandOffGenericMessage> receiveChat() throws ApiException;
    void sendChat(String msg) throws ApiException;
    void closeChat() throws ApiException;
}
