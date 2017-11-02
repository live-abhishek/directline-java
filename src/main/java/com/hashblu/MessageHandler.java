package com.hashblu;

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
//    IMessageQueue<HandOffGenericMessage> agentQueue;

    Map<String, AgentClientRunner> clientRunnerMap = new HashMap<>();

//    AgentClientRunner clientRunner;

    private static MessageHandler messageHandler;

    private MessageHandler(){
        botQueue = new CustomMessageQueue<>();
//        agentQueue = new CustomMessageQueue<>();
    }

    // agents will PUSH here
    public void handleAgentMessage(HandOffGenericMessage genMsg){
        botQueue.pushMsg(genMsg);
    }

    // direct-line will PUSH here
    public void handleBotMessage(HandOffGenericMessage genMsg) throws ApiException {
        if(genMsg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_START_FROM_USER){
            if(clientRunnerMap.size() < 5){
                AgentClientRunner agentClientRunner = createAgentClientRunner(genMsg.getChannelId());
                agentClientRunner.startHandoff();
                // create a message that chat with live agent has been successfully established
            } else {
                // create a message that all slots are full & put in bot Queue
            }
        } else if(genMsg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_USER){
            clientRunnerMap.get(genMsg.getChannelId()).endChat();
            clientRunnerMap.remove(genMsg.getChannelId());
//            clientRunner.endChat();
//            clientRunner = null;
        } else {
            clientRunnerMap.get(genMsg.getChannelId()).getAgentQueue().pushMsg(genMsg);
//            agentQueue.pushMsg(genMsg);
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

    private AgentClientRunner createAgentClientRunner(String channelId){
        return new AgentClientRunner(channelId, new LiveChatAgentClient(AppConstants.LIVE_CHAT_LICENCE_ID));
    }
}
