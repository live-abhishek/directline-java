package com.hashblu.agents;

import org.springframework.web.client.RestTemplate;

/**
 * Created by abhisheks on 18-Oct-17.
 */
public class AgentClient {

    IAgentClient agentClient;
    RestTemplate restTemplate;

    Thread receiverThread;
    boolean receiveFlag;

    public AgentClient(IAgentClient client){
        this.agentClient = client;
        RestTemplate restTemplate = new RestTemplate();
    }

    public void startReceivingMessage(){
        if(receiverThread != null){
            return;
        }

        receiverThread = new Thread(){
            @Override
            public void run(){
                while(true){
                    if(!receiveFlag)
                        break;
                    try{
                        agentClient.receiveChat();
                    } catch(Exception e){

                    }
                }
            }
        };
    }

    public void sendChat(String msg){
        agentClient.sendChat(msg);
    }


}
