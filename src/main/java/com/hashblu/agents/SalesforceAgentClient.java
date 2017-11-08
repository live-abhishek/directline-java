package com.hashblu.agents;

import com.hashblu.humanhandoff.AppConstants;
import com.hashblu.messages.HandOffGenericMessage;
import com.hashblu.messages.salesforce.SalesforceSystemMessageResponse;
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
    private SalesForceRestAgentClient sfRestClient;

    private SalesforceAgentClient(String orgId, String deploymentId, String chatButtonId, String apiVersion){
        this.baseUrl = String.format("%s/%s", AppConstants.SALESFORCE_URL, "chat/rest");
        this.orgId = orgId;
        this.deploymentId = deploymentId;
        this.chatButtonId = chatButtonId;
        this.apiVersion = apiVersion;
        this.sfRestClient = new SalesForceRestAgentClient(this.orgId, this.deploymentId, this.chatButtonId, this.apiVersion);
    }

    @Override
    public void startChat() {
        sfRestClient.getSessionId();
        try {
            Thread.sleep(1*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sfRestClient.chasitorInit();
        SalesforceSystemMessageResponse chatStatus = sfRestClient.getSystemMessage();
        // this may take a lot of time
        // if long time taken, discard this chat request altogether
        SalesforceSystemMessageResponse chatEstablishment = sfRestClient.getSystemMessage();
    }

    @Override
    public List<HandOffGenericMessage> receiveChat() {
        SalesforceSystemMessageResponse messages = sfRestClient.getSystemMessage();

    }

    @Override
    public void sendChat(String msg) {
        sfRestClient.chatMessage(msg);
    }

    @Override
    public void closeChat() {
        sfRestClient.chatEnd();
    }
}
