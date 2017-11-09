package com.hashblu.agents;

import com.hashblu.MessageHandler;
import com.hashblu.exceptions.AgentMessageRetrievalException;
import com.hashblu.messages.HandOffGenericMessage;
import com.hashblu.messages.queue.CustomMessageQueue;
import com.hashblu.messages.queue.IMessageQueue;
import io.swagger.client.ApiException;

import java.sql.Timestamp;
import java.util.Date;
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

    IMessageQueue<HandOffGenericMessage> agentQueue;

    public String conversationId;
    public long lastMessageTime;

    private int messageRetirevalRetryCount = 0;

    public AgentClientRunner(String conversationId, IAgentClient client){
        this.agentClient = client;
        this.agentQueue = new CustomMessageQueue<>();
        this.conversationId = conversationId;
    }

    public void startHandoff() throws ApiException {
        try {
            agentClient.startChat();
            HandOffGenericMessage genMsg = createHandoffGenericMessage(HandOffGenericMessage.MessageType.CHAT_START_FROM_USER_SUCCESS, "");
            lastMessageTime = System.currentTimeMillis();
            MessageHandler.getMessageHandler().handleAgentMessage(genMsg);
            startReceivingRemoteMessage();
            startReceivingQueueMessage();
        } catch (Exception e){
            HandOffGenericMessage genMsg = createHandoffGenericMessage(HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT, e.getMessage());
            lastMessageTime = System.currentTimeMillis();
            MessageHandler.getMessageHandler().handleAgentMessage(genMsg);
        }
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
                            m.setConversationId(AgentClientRunner.this.conversationId);
                            MessageHandler.getMessageHandler().handleAgentMessage(m);
                            if(m.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT){
                                try {
                                    endChat();
                                } catch (ApiException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        messageRetirevalRetryCount = 0;
                        Thread.sleep(1*1000);
                    } catch (Exception e){
                        messageRetirevalRetryCount++;
                        handleMaxRetry();
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveRemoteFlag = true;
        receiverRemoteThread.setName("Agent Receiver: " + agentClient.getClass().getSimpleName());
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
                        HandOffGenericMessage msg = MessageHandler.getMessageHandler().getAgentMessage(AgentClientRunner.this);
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

    public IMessageQueue<HandOffGenericMessage> getAgentQueue(){
        return agentQueue;
    }

    private HandOffGenericMessage createHandoffGenericMessage(HandOffGenericMessage.MessageType msgType, String msg){
        HandOffGenericMessage genericMessage = new HandOffGenericMessage(msgType, msg);
        genericMessage.setConversationId(AgentClientRunner.this.conversationId);
        return genericMessage;
    }

    private void handleMaxRetry() {
        if(messageRetirevalRetryCount >= 5){
            // create & send message & endChat
            HandOffGenericMessage genericMessage = createHandoffGenericMessage(HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT, "");
            MessageHandler.getMessageHandler().handleAgentMessage(genericMessage);
            try {
                endChat();
            } catch (ApiException a) {
                a.printStackTrace();
            }
        }
    }
}
