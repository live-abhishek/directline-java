package com.hashblu.agents;

import com.hashblu.MessageHandler;
import com.hashblu.messages.messageListener.DumperMessageListener;
import com.hashblu.messages.messageListener.IReceiveMessageListener;
import com.hashblu.messages.HandOffGenericMessage;
import com.hashblu.messages.queue.IMessageQueue;
import io.swagger.client.ApiException;

import java.util.List;

/**
 * Created by abhisheks on 18-Oct-17.
 */
public class AgentClientRunner {

    IAgentClient agentClient;

    Thread receiverRemoteThread;
    boolean receiveRemoteFlag;

    Thread receiverQueueThread;
    boolean receiveQueueFlag;

    public AgentClientRunner(IAgentClient client){
        this.agentClient = client;
    }

    public void startHandoff() throws ApiException {
        agentClient.startChat();
        startReceivingRemoteMessage();
        startReceivingQueueMessage();
    }

    private void startReceivingRemoteMessage() throws ApiException {
        receiverRemoteThread = new Thread(){
            @Override
            public void run(){
                while(true){
                    if(!receiveRemoteFlag) {
                        break;
                    }
                    try{
                        List<HandOffGenericMessage> genericMsgs = agentClient.receiveChat();
                        genericMsgs.forEach(m -> {
                            MessageHandler.getMessageHandler().handleAgentMessage(m);
                            if(m.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT){
                                try {
                                    endChat();
                                } catch (ApiException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread.sleep(1*1000);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveRemoteFlag = true;
        receiverRemoteThread.setName("Agent Reciever: " + agentClient.getClass().getSimpleName());
        receiverRemoteThread.start();
    }

    private void startReceivingQueueMessage() throws ApiException {
        receiverQueueThread = new Thread() {
            @Override
            public void run(){
                while(true){
                    if(!receiveQueueFlag){
                        break;
                    }
                    try{
                        HandOffGenericMessage msg = MessageHandler.getMessageHandler().getAgentMessage();
                        if(msg != null) {
                            agentClient.sendChat(msg.getMsg());
                            if(msg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_USER){
                                endChat();
                            }
                        }
                        Thread.sleep(1*1000);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveQueueFlag = true;
        receiverQueueThread.setName("Agent Sender: " + agentClient.getClass().getSimpleName());
        receiverQueueThread.start();
    }

    public void endChat() throws ApiException {
        receiveRemoteFlag = false;
        receiveQueueFlag = false;
        try{
            receiverRemoteThread.join();
            receiverQueueThread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        agentClient.closeChat();
    }


}
