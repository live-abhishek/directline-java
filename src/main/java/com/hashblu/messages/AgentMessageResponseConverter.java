package com.hashblu.messages;

import com.hashblu.messages.livechat.LiveChatPendingMessageResponse;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by abhisheks on 23-Oct-17.
 */
public class AgentMessageResponseConverter {
    public static List<HandOffGenericMessage> process(LiveChatPendingMessageResponse msgResponse){
        List<HandOffGenericMessage> handOffGenericMessages = new ArrayList<>();
        for(LiveChatPendingMessageResponse.MessageResponse msgRes : msgResponse.getEvents()){
            if(msgRes.getText() != null && !msgRes.getUser_type().equals("visitor")) {
                HandOffGenericMessage genericMessage = new HandOffGenericMessage(HandOffGenericMessage.MessageType.CHAT_TEXT_FROM_AGENT , msgRes.getText());
                genericMessage.setTimeStamp(new Timestamp(new Date().getTime()));
                handOffGenericMessages.add(genericMessage);
            }
            if(msgRes.getType().equals("chat_closed")){
                HandOffGenericMessage genericMessage = new HandOffGenericMessage(HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT, "");
                genericMessage.setTimeStamp(new Timestamp(new Date().getTime()));
                handOffGenericMessages.add(genericMessage);
            }
        }
        return handOffGenericMessages;
    }
}
