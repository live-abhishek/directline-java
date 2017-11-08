package com.hashblu.messages.salesforce;

import java.util.List;

/**
 * Created by abhisheks on 08-Nov-17.
 */
public class SalesforceSystemMessageResponse {
    private List<MessageType> messages;
    private long sequence;
    private long offset;

    public List<MessageType> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageType> messages) {
        this.messages = messages;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public static class MessageType{
        // ChatRequestSuccess, ChatEstablished, AgentTyping, AgentNotTyping, ChatMessage, ChatRequestFail
        private String type;
        private Message message;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    public static class Message{
        private String name;
        private String userId;
        private List<String> items;
        private boolean sneakPeekEnabled;
        private String text;
        private String agentId;
        private String visitorId;
        private String connectionTimeout;
        private String reason;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }

        public boolean isSneakPeekEnabled() {
            return sneakPeekEnabled;
        }

        public void setSneakPeekEnabled(boolean sneakPeekEnabled) {
            this.sneakPeekEnabled = sneakPeekEnabled;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getVisitorId() {
            return visitorId;
        }

        public void setVisitorId(String visitorId) {
            this.visitorId = visitorId;
        }

        public String getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(String connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }
    }
}
