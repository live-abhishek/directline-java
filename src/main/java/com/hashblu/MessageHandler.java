package com.hashblu;

import com.hashblu.agents.AgentClientRunner;
import com.hashblu.agents.LiveChatAgentClient;
import com.hashblu.humanhandoff.AppConstants;
import com.hashblu.messages.HandOffGenericMessage;
import com.hashblu.messages.queue.CustomMessageQueue;
import com.hashblu.messages.queue.IMessageQueue;
import io.swagger.client.ApiException;

/**
 * Created by abhisheks on 26-Oct-17.
 */
public class MessageHandler {
    IMessageQueue<HandOffGenericMessage> botQueue;
    IMessageQueue<HandOffGenericMessage> agentQueue;

    AgentClientRunner clientRunner;

    private static MessageHandler messageHandler;

    private MessageHandler(){
        botQueue = new CustomMessageQueue<>();
        agentQueue = new CustomMessageQueue<>();
    }

    // direct-line will PUSH here
    public void handleAgentMessage(HandOffGenericMessage genMsg){
        botQueue.pushMsg(genMsg);
    }

    // agents will PUSH here
    public void handleBotMessage(HandOffGenericMessage genMsg) throws ApiException {
        if(genMsg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_START_FROM_USER){
            if(clientRunner != null){
                System.out.println("Agent is already on!");
                return;
            }
            clientRunner = new AgentClientRunner(new LiveChatAgentClient(AppConstants.LIVE_CHAT_LICENCE_ID));
            clientRunner.startHandoff();
        } else if(genMsg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_USER){
            clientRunner.endChat();
            clientRunner = null;
        } else {
            agentQueue.pushMsg(genMsg);
        }
        // logic to start agent connections & divert messages to them
    }

    // agents will PULL here
    public HandOffGenericMessage getAgentMessage(){
        return agentQueue.getMsg();
    }

    // direct-line will PULL here
    public HandOffGenericMessage getBotMessage(){
        return botQueue.getMsg();
    }

    public static MessageHandler getMessageHandler(){
        if(messageHandler == null){
            messageHandler = new MessageHandler();
        }
        return messageHandler;
    }

}
