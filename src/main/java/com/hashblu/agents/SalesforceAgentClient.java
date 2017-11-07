package com.hashblu.agents;

import com.hashblu.humanhandoff.AppConstants;
import com.hashblu.messages.HandOffGenericMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * Created by abhisheks on 16-Oct-17.
 */
public class SalesforceAgentClient implements IAgentClient {

    private String baseUrl;
    private String orgId;
    private String deploymentId;
    private String chatButtonId;
    private String apiVersion;

    private SalesforceAgentClient(String orgId, String deploymentId, String chatButtonId, String apiVersion){
        this.baseUrl = String.format("%s/%s", AppConstants.SALESFORCE_URL, "chat/rest");
    }

    @Override
    public void startChat() {
        String startChatUrl = String.format("%s/System/SessionId");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-LIVEAGENT-AFFINITY", null);
        headers.set("X-LIVEAGENT-API-VERSION", apiVersion);

        HttpEntity requestEntity = new HttpEntity<>(headers);
        

    }

    @Override
    public List<HandOffGenericMessage> receiveChat() {
        return null;
    }

    @Override
    public void sendChat(String msg) {

    }

    @Override
    public void closeChat() {

    }
}
