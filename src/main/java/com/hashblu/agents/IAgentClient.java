package com.hashblu.agents;

import io.swagger.client.ApiException;

/**
 * Created by abhisheks on 13-Oct-17.
 */
public interface IAgentClient {
    void startChat() throws ApiException;
    String receiveChat() throws ApiException;
    void sendChat(String msg) throws ApiException;
    void closeChat() throws ApiException;
}
