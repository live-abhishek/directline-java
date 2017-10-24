package com.hashblu.agents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhisheks on 23-Oct-17.
 */
public class AgentMessageResponseConverter {
    public static List<HandOffGenericMessage> process(LiveChatPendingMessageResponse msgResponse){
        List<HandOffGenericMessage> handOffGenericMessages = new ArrayList<>();
        for(LiveChatPendingMessageResponse.MessageResponse msgRes : msgResponse.getEvents()){
            if(msgRes.getText() != null && !msgRes.getUser_type().equals("visitor")) {
                handOffGenericMessages.add(new HandOffGenericMessage(msgRes.getText()));
            }
            if(msgRes.getType().equals("chat_closed")){
                handOffGenericMessages.add(new HandOffGenericMessage(HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT, ""));
            }
        }
        return handOffGenericMessages;
    }
}
