package com.hashblu.messages;

/**
 * Created by abhisheks on 23-Oct-17.
 */
public class HandOffGenericMessage {
    private MessageType msgType;
    private String msg;
    private String channelId;

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

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public enum MessageType{
        CHAT_END_FROM_AGENT,
        CHAT_END_FROM_USER,
        TEXT_MSG,
        CHAT_START_FROM_USER
    }
}
