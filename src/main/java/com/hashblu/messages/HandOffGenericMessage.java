package com.hashblu.messages;

/**
 * Created by abhisheks on 23-Oct-17.
 */
public class HandOffGenericMessage {
    private MessageType msgType;
    private String msg;

    public HandOffGenericMessage(MessageType msgType, String msg){
        this.msgType = msgType;
        this.msg = msg;
    }

    public HandOffGenericMessage(String msg){
        this(MessageType.TEXT_MSG, msg);
    }

    public String getMsg() {
        return msg;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    public enum MessageType{
        CHAT_END_FROM_AGENT,
        CHAT_END_FROM_USER,
        TEXT_MSG,
        CHAT_START_FROM_USER
    }
}
