package com.hashblu.agents;

import com.hashblu.humanhandoff.AppConstants;
import com.hashblu.messages.HandOffGenericMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by abhisheks on 16-Oct-17.
 */
public class SalesforceAgentClient extends AbsAgentClient {

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
