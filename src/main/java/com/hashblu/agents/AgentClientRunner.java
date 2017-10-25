package com.hashblu.agents;

import com.hashblu.messages.messageListener.IReceiveMessageListener;
import com.hashblu.messages.HandOffGenericMessage;
import io.swagger.client.ApiException;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by abhisheks on 18-Oct-17.
 */
public class AgentClientRunner {

    IAgentClient agentClient;

    Thread receiverThread;
    boolean receiveFlag;

    Queue<String> msgQueue;
    IReceiveMessageListener msgListener;

    public AgentClientRunner(IAgentClient client, IReceiveMessageListener msgListener){
        this.agentClient = client;
        this.msgListener = msgListener;
        msgQueue = new LinkedList<>();
    }

    public void startReceivingMessage() throws ApiException {
        if(receiverThread != null){
            return;
        }

        this.agentClient.startChat();

        receiverThread = new Thread(){
            @Override
            public void run(){
                while(true){
                    if(!receiveFlag)
                        break;
                    try{
                        List<HandOffGenericMessage> genericMsgs = agentClient.receiveChat();
                        genericMsgs.forEach(m -> {
                            msgListener.processMessage(m);
                            if(m.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT){
                                try {
                                    endChat();
                                } catch (ApiException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread.sleep(2*1000);
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }
        };
        receiveFlag = true;
        receiverThread.setName("Reciever: " + agentClient.getClass().getSimpleName());
        receiverThread.start();
    }

    public void sendChat(String msg) throws ApiException {
        agentClient.sendChat(msg);
    }

    public void endChat() throws ApiException {
        receiveFlag = false;
        try{
            receiverThread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        agentClient.closeChat();
    }


}
