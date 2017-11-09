package com.hashblu.agents;

import com.hashblu.exceptions.AgentConnectionException;
import com.hashblu.exceptions.AgentMessageRetrievalException;
import com.hashblu.humanhandoff.AppConstants;
import com.hashblu.messages.AgentMessageResponseConverter;
import com.hashblu.messages.HandOffGenericMessage;
import com.hashblu.messages.salesforce.SalesforceSystemMessageResponse;

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
    private String visitorName;
    private SalesForceRestAgentClient sfRestClient;

    public SalesforceAgentClient(String orgId, String deploymentId, String chatButtonId, String apiVersion, String visitorName){
        this.baseUrl = String.format("%s/%s", AppConstants.SALESFORCE_URL, "chat/rest");
        this.orgId = orgId;
        this.deploymentId = deploymentId;
        this.chatButtonId = chatButtonId;
        this.apiVersion = apiVersion;
        this.visitorName = visitorName;
        this.sfRestClient = new SalesForceRestAgentClient(this.orgId, this.deploymentId, this.chatButtonId, this.apiVersion, this.baseUrl, this.visitorName);
    }

    @Override
    public void startChat() {
        try {
            sfRestClient.getSessionId();
            try {
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String responseString = sfRestClient.chasitorInit();
            if (!responseString.toLowerCase().equals("OK".toLowerCase())) {
                throw new AgentConnectionException("Error in Chasitor init");
            }

            SalesforceSystemMessageResponse chatStatus = sfRestClient.getSystemMessage();
            if (!chatStatus.getMessages().get(0).getType().equals("ChatRequestSuccess")) {
                throw new AgentConnectionException("Error in Chat status");
            }
            // this may take a lot of time
            // if long time taken, discard this chat request altogether
            SalesforceSystemMessageResponse chatEstablishment = sfRestClient.getSystemMessage();
            if (!chatEstablishment.getMessages().get(0).getType().equals("ChatEstablished")) {
                throw new AgentConnectionException("Error in Chat Establishment");
            }
        } catch(AgentConnectionException e){
            throw e;
        } catch(Exception e){
            throw new AgentConnectionException(e);
        }
    }

    @Override
    public List<HandOffGenericMessage> receiveChat() {
        try {
            SalesforceSystemMessageResponse messages = sfRestClient.getSystemMessage();
            List<HandOffGenericMessage> genMessages = AgentMessageResponseConverter.process(messages);
            return genMessages;
        } catch (Exception e){
            throw new AgentMessageRetrievalException("Error while retrieving messages", e);
        }
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
