package com.hashblu.agents;

import java.util.List;

/**
 * Created by abhisheks on 23-Oct-17.
 */
public class LiveChatPendingMessageResponse {

    private List<MessageResponse> events;

    public List<MessageResponse> getEvents() {
        return events;
    }

    public void setEvents(List<MessageResponse> messageResponses) {
        this.events = messageResponses;
    }

    public static class MessageResponse{
        private Agent agent;
        private long message_id;
        private String text;
        private long timestamp;
        private String type;
        private String user_type;

        public long getMessage_id() {
            return message_id;
        }

        public void setMessage_id(long message_id) {
            this.message_id = message_id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }

    public static class Agent {
        private String avatar;
        private String job_title;
        private String name;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getJob_title() {
            return job_title;
        }

        public void setJob_title(String job_title) {
            this.job_title = job_title;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
