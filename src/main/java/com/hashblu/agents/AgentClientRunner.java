package com.hashblu.agents;

import com.hashblu.MessageHandler;
import com.hashblu.humanhandoff.AppConstants;
import com.hashblu.messages.HandOffGenericMessage;
import com.hashblu.messages.queue.CustomMessageQueue;
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

    IMessageQueue<HandOffGenericMessage> agentQueue;

    public String conversationId;
    public long lastMessageTime;

    private int messageRetrievalRetryCount = 0;
    private long userMsgWaitTime = AppConstants.USER_MESSAGE_WAIT_PERIOD_MIN;

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
                    });
                    messageRetrievalRetryCount = 0;
                    Thread.sleep(1*1000);
                } catch (Exception e){
                    messageRetrievalRetryCount++;
                    handleMaxRetry();
                    e.printStackTrace();
                }
            }
            }
        };
        receiveRemoteFlag = true;
        receiverRemoteThread.setName(String.format("Agent Receiver %s", this.conversationId) + agentClient.getClass().getSimpleName());
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
                    if(System.currentTimeMillis() >= lastMessageTime + userMsgWaitTime * 60 * 1000){ // end chat if user a has not sent any chat for some time
                        HandOffGenericMessage genMsg = createHandoffGenericMessage(HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT, "No new message from user.");
                        MessageHandler.getMessageHandler().handleAgentMessage(genMsg);
                    }
                    try{
                        HandOffGenericMessage msg = MessageHandler.getMessageHandler().getAgentMessage(AgentClientRunner.this);
                        if(msg != null) {
                            lastMessageTime = System.currentTimeMillis();
                            agentClient.sendChat(msg.getMsg());
                        }
                        Thread.sleep(1*1000);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveQueueFlag = true;
        receiverQueueThread.setName(String.format("Agent Sender %s", this.conversationId) + agentClient.getClass().getSimpleName());
        receiverQueueThread.start();
    }

    public void endChat() {
        receiveRemoteFlag = false;
        receiveQueueFlag = false;
        try{
            agentClient.closeChat();
            if(receiverRemoteThread!= null && Thread.currentThread() != receiverRemoteThread) {
                receiverRemoteThread.join(2 * 1000);
            }
            if(receiverQueueThread != null && Thread.currentThread() != receiverQueueThread) {
                receiverQueueThread.join(2 * 1000);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
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
        if(messageRetrievalRetryCount >= 5){
            // create & send message
            HandOffGenericMessage genericMessage = createHandoffGenericMessage(HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT, "");
            MessageHandler.getMessageHandler().handleAgentMessage(genericMessage);
        }
    }
}
