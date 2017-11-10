package com.hashblu;

import com.hashblu.agents.AgentClientProvider;
import com.hashblu.agents.AgentClientRunner;
import com.hashblu.agents.LiveChatAgentClient;
import com.hashblu.humanhandoff.AppConstants;
import com.hashblu.messages.HandOffGenericMessage;
import com.hashblu.messages.queue.CustomMessageQueue;
import com.hashblu.messages.queue.IMessageQueue;
import io.swagger.client.ApiException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhisheks on 26-Oct-17.
 */
public class MessageHandler {
    IMessageQueue<HandOffGenericMessage> botQueue;
    Map<String, AgentClientRunner> clientRunnerMap = new HashMap<>();

    private static MessageHandler messageHandler;

    private MessageHandler(){
        botQueue = new CustomMessageQueue<>();
    }

    // agents will PUSH here
    public void handleAgentMessage(HandOffGenericMessage genMsg){
        botQueue.pushMsg(genMsg);
        if(genMsg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT){
            AgentClientRunner clientRunner = clientRunnerMap.get(genMsg.getConversationId());
            if(clientRunner != null) {
                clientRunner.endChat();
                clientRunnerMap.remove(genMsg.getConversationId());
            }
        }
    }

    // direct-line will PUSH here
    public void handleBotMessage(HandOffGenericMessage genMsg) throws ApiException {
        if(genMsg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_START_FROM_USER){
            if(clientRunnerMap.size() < AppConstants.MAX_SIMULTANEOUS_USERS){
                AgentClientRunner agentClientRunner = createAgentClientRunner(genMsg.getConversationId());
                clientRunnerMap.put(genMsg.getConversationId(), agentClientRunner);
                agentClientRunner.startHandoff();
            } else {
                // create a message that all slots are full & put in bot Queue
            }
        } else if(genMsg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_USER){
            clientRunnerMap.get(genMsg.getConversationId()).endChat();
            clientRunnerMap.remove(genMsg.getConversationId());
        } else {
            clientRunnerMap.get(genMsg.getConversationId()).getAgentQueue().pushMsg(genMsg);
        }
        // logic to start agent connections & divert messages to them
    }

    // agents will PULL here
    public HandOffGenericMessage getAgentMessage(AgentClientRunner clientRunner){
        return clientRunner.getAgentQueue().getMsg();
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

    private AgentClientRunner createAgentClientRunner(String conversationId){
        return new AgentClientRunner(conversationId, AgentClientProvider.getAgentClient());
    }
}
