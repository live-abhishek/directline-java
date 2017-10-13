package com.hashblu.agents;

/**
 * Created by abhisheks on 13-Oct-17.
 */
public interface IAgentClient {
    void startChat();
    void recievedChatListener();
    void sendChat();
    void closeChat();
}
