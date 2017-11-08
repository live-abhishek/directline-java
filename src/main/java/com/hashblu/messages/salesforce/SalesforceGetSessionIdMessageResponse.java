package com.hashblu.messages.salesforce;

/**
 * Created by abhisheks on 08-Nov-17.
 */
public class SalesforceGetSessionIdMessageResponse {
    private String key;
    private String id;
    private long clientPollTimeout;
    private String affinityToken;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getClientPollTimeout() {
        return clientPollTimeout;
    }

    public void setClientPollTimeout(long clientPollTimeout) {
        this.clientPollTimeout = clientPollTimeout;
    }

    public String getAffinityToken() {
        return affinityToken;
    }

    public void setAffinityToken(String affinityToken) {
        this.affinityToken = affinityToken;
    }
}
