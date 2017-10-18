package com.hashblu.agents;

/**
 * Created by abhisheks on 16-Oct-17.
 */
public class LiveChatAgentClient extends AbsAgentClient {

    String watermark;
    String baseUrl = "https://api.livechatinc.com/visitors/";
    String visitorId = "123456";

    @Override
    public void startChat() {
        String startChatUrl = "chat/start";
//        restTemplate.
    }

    @Override
    public String receiveChat() {
        return null;
    }

    @Override
    public void sendChat(String msg) {

    }

    @Override
    public void closeChat() {

    }
}
