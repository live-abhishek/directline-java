package com.hashblu.agents;

import com.hashblu.messages.*;
import com.hashblu.messages.livechat.LiveChatGenericMessageResponse;
import com.hashblu.messages.livechat.LiveChatPendingMessageResponse;
import com.hashblu.messages.livechat.LiveChatStartMessageResponse;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by abhisheks on 16-Oct-17.
 */
public class LiveChatAgentClient extends AbsAgentClient {

    long watermark;
    String baseUrl;
    String visitorId;
    String licenceId;
    String secureSessionId;

    public LiveChatAgentClient(String licenceId){
        this.licenceId = licenceId;
        this.visitorId = Integer.toString(Math.abs(new Random().nextInt()));
        this.baseUrl = String.format("https://api.livechatinc.com/visitors/%s", this.visitorId);
    }

    @Override
    public void startChat() {
        String startChatUrl = String.format("%s/chat/start", baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-API-Version", "2");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("licence_id", licenceId);
        body.add("welcome_message", "Hi");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<LiveChatStartMessageResponse> responseEntity = restTemplate.exchange(
                startChatUrl, HttpMethod.POST, requestEntity, LiveChatStartMessageResponse.class);
        this.secureSessionId = responseEntity.getBody().getSecured_session_id();
    }

    @Override
    public List<HandOffGenericMessage> receiveChat() {
        if(secureSessionId == null){
            throw new RuntimeException("SecuredSessionId is null");
        }
        String receiveChatUrl = String.format("%s/chat/get_pending_messages?licence_id=%s&secured_session_id=%s&last_message_id=%s", baseUrl, licenceId, secureSessionId, watermark);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("X-API-Version", "2");


        HttpEntity requestEntity = new HttpEntity<>(headers);
        ResponseEntity<LiveChatPendingMessageResponse> responseEntity = restTemplate.exchange(receiveChatUrl, HttpMethod.GET, requestEntity, LiveChatPendingMessageResponse.class);
        LiveChatPendingMessageResponse messageResponses = responseEntity.getBody();
        messageResponses.getEvents().forEach(m -> {
            if(watermark < m.getMessage_id()){
                watermark = m.getMessage_id();
            }
        });
        return AgentMessageResponseConverter.process(messageResponses);
    }

    @Override
    public void sendChat(String msg) {
        if(secureSessionId == null){
            throw new RuntimeException("SecuredSessionId is null");
        }
        String sendChatUrl = String.format("%s/chat/send_message", baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-API-Version", "2");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("licence_id", licenceId);
        body.add("secured_session_id", secureSessionId);
        body.add("message", msg);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.exchange(sendChatUrl, HttpMethod.POST, requestEntity, LiveChatGenericMessageResponse.class);
    }

    @Override
    public void closeChat() {
        if(secureSessionId == null){
            throw new RuntimeException("SecuredSessionId is null");
        }
        String endChatUrl = String.format("%s/chat/close", baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-API-Version", "2");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("licence_id", licenceId);
        body.add("secured_session_id", secureSessionId);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.exchange(endChatUrl, HttpMethod.POST, requestEntity, LiveChatGenericMessageResponse.class);
    }
}
