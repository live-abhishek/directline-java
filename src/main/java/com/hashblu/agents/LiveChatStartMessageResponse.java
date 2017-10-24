package com.hashblu.agents;

/**
 * Created by abhisheks on 23-Oct-17.
 */
public class LiveChatStartMessageResponse {
    private String secured_session_id;
    private boolean banned;

    public String getSecured_session_id() {
        return secured_session_id;
    }

    public void setSecured_session_id(String secured_session_id) {
        this.secured_session_id = secured_session_id;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
