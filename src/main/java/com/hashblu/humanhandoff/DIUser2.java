package com.hashblu.humanhandoff;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ConversationsApi;
import io.swagger.client.model.*;

/**
 * Created by abhisheks on 04-Oct-17.
 */
public class DIUser2 {

    private final ApiClient client;

    public enum MessageType{
        Message("message"),
        EndOfConversation("endOfConversation");

        private String msg = null;
        MessageType(String msg){
            this.msg = msg;
        }

        public String getMessageString(){
            return this.msg;
        }
    }

    private static final String CHANNEL_ACCOUNT_NAME = "John Smith";
    private static final String CHANNEL_ID = "Live-Agent";
    private static final String apiKey = AppConstants.DIRECT_LINE_KEY1;
    private final ConversationsApi conversations;
    private final Conversation conversation;
    private volatile boolean receiveFlag = false;
    private Thread receiverThread;

    public DIUser2() throws ApiException {
        client = new ApiClient();
        client.addDefaultHeader("Authorization", "Bearer " + apiKey);
        conversations = new ConversationsApi(client);
        conversation = conversations.conversationsStartConversation();
    }

    public void startReceivingMessage(){
        if( receiverThread != null )
            return;

        receiverThread = new Thread(){
            @Override
            public void run() {
                String watermark = null;
                while(true) {
                    if (!receiveFlag)
                        break;
                    try {
                        ActivitySet activitySet = conversations.conversationsGetActivities(conversation.getConversationId(), watermark);
                        watermark = activitySet.getWatermark();
                        activitySet.getActivities().forEach(a -> {
                            if(!a.getFrom().getId().equals("Live-Agent")){
                                System.out.println(a.getText());
                            }
                        });
                        Thread.sleep(1000 * 1);
                    } catch (ApiException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("Message receiving ended!");
            }
        };
        receiveFlag = true;
        receiverThread.start();
    }

    public void stopConversation(){
        receiveFlag = false;
        try {
            receiverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            sendEndOfConversationMessage();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws ApiException {
        Activity activity = new Activity();
        ChannelAccount channelAccount = new ChannelAccount();
        channelAccount.setName(CHANNEL_ACCOUNT_NAME);
        channelAccount.setId(CHANNEL_ID);
        activity.setFrom(channelAccount);
        activity.setType(MessageType.Message.getMessageString());
        activity.setText(message);
        ResourceResponse response = conversations.conversationsPostActivity(conversation.getConversationId(), activity);
    }

    private void sendEndOfConversationMessage() throws ApiException {
        Activity activity = new Activity();
        ChannelAccount channelAccount = new ChannelAccount();
        channelAccount.setName(CHANNEL_ACCOUNT_NAME);
        channelAccount.setId(CHANNEL_ID);
        activity.setFrom(channelAccount);
        activity.setType(MessageType.EndOfConversation.getMessageString());
        ResourceResponse response = conversations.conversationsPostActivity(conversation.getConversationId(), activity);
    }

}
