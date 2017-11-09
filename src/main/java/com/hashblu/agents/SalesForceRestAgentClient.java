package com.hashblu.agents;

import com.hashblu.messages.salesforce.SalesforceChasitorInitBody;
import com.hashblu.messages.salesforce.SalesforceGetSessionIdMessageResponse;
import com.hashblu.messages.salesforce.SalesforceSystemMessageResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhisheks on 08-Nov-17.
 */
public class SalesForceRestAgentClient {
    private RestTemplate restTemplate;
    private String baseUrl;
    private String orgId;
    private String deploymentId;
    private String chatButtonId;
    private String apiVersion;

    private String sessionKey;
    private String affinity;
    private String sessionId;
    private long sequence;
    private String visitorName;

    public SalesForceRestAgentClient(String orgId, String deploymentId, String chatButtonId, String apiVersion, String baseUrl, String visitorName){
        this.restTemplate = new RestTemplate();
        this.orgId = orgId;
        this.deploymentId = deploymentId;
        this.chatButtonId = chatButtonId;
        this.apiVersion = apiVersion;
        this.sequence = 0;
        this.baseUrl = baseUrl;
        this.visitorName = visitorName;
    }

    public void getSessionId(){
        String startChatUrl = String.format("%s/System/SessionId", baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-LIVEAGENT-AFFINITY", "null");
        headers.set("X-LIVEAGENT-API-VERSION", apiVersion);

        HttpEntity requestEntity = new HttpEntity<>(headers);

        ResponseEntity<SalesforceGetSessionIdMessageResponse> responseEntity = restTemplate.exchange(startChatUrl, HttpMethod.GET, requestEntity, SalesforceGetSessionIdMessageResponse.class);
        this.sessionKey = responseEntity.getBody().getKey();
        this.affinity = responseEntity.getBody().getAffinityToken();
        this.sessionId = responseEntity.getBody().getId();
        this.sequence = 1;
    }

    public String chasitorInit() {
        String chasitorInitUrl = String.format("%s/Chasitor/ChasitorInit", baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-liveagent-affinity", affinity);
        headers.set("x-liveagent-api-version", apiVersion);
        headers.set("x-liveagent-session-key", sessionKey);
        headers.set("x-liveagent-sequence", Long.toString(sequence));
        headers.set("Content-Type", "application/json");
        headers.set("cache-control", "no-cache");

        SalesforceChasitorInitBody chasitorInitBody = new SalesforceChasitorInitBody(this.orgId, this.deploymentId, this.chatButtonId, this.sessionId, this.visitorName);
        HttpEntity<SalesforceChasitorInitBody> requestEntity = new HttpEntity(chasitorInitBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(chasitorInitUrl, requestEntity, String.class);

        sequence++;
        return responseEntity.getBody();
    }

    public String chatMessage(String msg){
        String chatMsgUrl = String.format("%s/Chasitor/ChatMessage", baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-LIVEAGENT-AFFINITY", affinity);
        headers.set("X-LIVEAGENT-API-VERSION", apiVersion);
        headers.set("X-LIVEAGENT-SESSION-KEY", sessionKey);
        headers.set("X-LIVEAGENT-SEQUENCE", Long.toString(sequence));

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("text", msg);
        HttpEntity requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(chatMsgUrl, HttpMethod.POST, requestEntity, String.class);
        sequence++;
        return responseEntity.getBody();
    }

    public String chatEnd(){
        String chatEndUrl = String.format("%s/Chasitor/ChatEnd", baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-LIVEAGENT-AFFINITY", affinity);
        headers.set("X-LIVEAGENT-API-VERSION", apiVersion);
        headers.set("X-LIVEAGENT-SESSION-KEY", sessionKey);
        headers.set("X-LIVEAGENT-SEQUENCE", Long.toString(sequence));

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("reason", "client");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(chatEndUrl, HttpMethod.POST, requestEntity, String.class);
        return responseEntity.getBody();
    }

    public SalesforceSystemMessageResponse getSystemMessage(){
        String sysMsgUrl = String.format("%s/System/Messages", baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-LIVEAGENT-AFFINITY", affinity);
        headers.set("X-LIVEAGENT-API-VERSION", apiVersion);
        headers.set("X-LIVEAGENT-SESSION-KEY", sessionKey);

        HttpEntity requestEntity = new HttpEntity<>(headers);
        ResponseEntity<SalesforceSystemMessageResponse> responseEntity = restTemplate.exchange(sysMsgUrl, HttpMethod.GET, requestEntity, SalesforceSystemMessageResponse.class);
        return responseEntity.getBody();
    }
}
