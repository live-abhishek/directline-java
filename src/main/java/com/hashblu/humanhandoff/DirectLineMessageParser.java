package com.hashblu.humanhandoff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hashblu.messages.HandOffGenericMessage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhisheks on 03-Nov-17.
 */
public class DirectLineMessageParser {
    ObjectMapper mapper = new ObjectMapper();

    public HandOffGenericMessage parseMsg(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = mapper.readValue(jsonString, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HandOffGenericMessage genericMessage = new HandOffGenericMessage("");
        genericMessage.setConversationId(map.get("conversationId").toString());
        genericMessage.setTimeStamp(new Timestamp(Long.parseLong(map.get("msgTimeStamp").toString())));
        if(map.get("msgCommand").equals("CHAT_START_FROM_USER")){
            genericMessage.setMsgType(HandOffGenericMessage.MessageType.CHAT_START_FROM_USER);
        } else if(map.get("msgCommand").equals("CHAT_END_FROM_USER")) {
            genericMessage.setMsgType(HandOffGenericMessage.MessageType.CHAT_END_FROM_USER);
        } else if(map.get("msgCommand").equals("CHAT_TEXT_FROM_USER")) {
            genericMessage.setMsgType(HandOffGenericMessage.MessageType.TEXT_MSG);
            genericMessage.setMsg(map.get("msgText").toString());
        }
        return genericMessage;
    }

    public String parseHandoffMessage(HandOffGenericMessage genericMessage){
        Map<String, Object> map = new HashMap<>();
        map.put("conversationId", genericMessage.getConversationId().toString());
        map.put("msgTimeStamp", genericMessage.getTimeStamp().getTime());
        map.put("msgCommand", genericMessage.getMsgType());
        map.put("msgText", genericMessage.getMsg());
        if(genericMessage.getMsgType() == HandOffGenericMessage.MessageType.CHAT_START_FROM_USER_SUCCESS){
            map.put("msgCommand", HandOffGenericMessage.MessageType.CHAT_START_FROM_USER_SUCCESS.toString());
        } else if(genericMessage.getMsgType() == HandOffGenericMessage.MessageType.TEXT_MSG){
            map.put("msgCommand", "CHAT_TEXT_FROM_AGENT");
        }
        String jsonString = "{}";
        try{
            jsonString = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
