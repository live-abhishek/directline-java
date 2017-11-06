package com.hashblu.humanhandoff;

import com.hashblu.MessageHandler;
import com.hashblu.messages.HandOffGenericMessage;
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

    private static final String CHANNEL_ACCOUNT_NAME = AppConstants.DIRECTLINE_CHANNEL_ACCOUNT_NAME;
    private static final String CHANNEL_ID = AppConstants.DIRECTLINE_CHANNEL_ID;
    private static final String apiKey = AppConstants.DIRECT_LINE_KEY1;
    private final ConversationsApi conversations;
    private final Conversation conversation;
    private volatile boolean receiveRemoteFlag = false;
    private volatile boolean receiveQueueFlag = false;
    private Thread receiverRemoteThread;
    private Thread receiverQueueThread;

    private DirectLineMessageParser directLineMessageParser;

    public DIUser2() throws ApiException {
        client = new ApiClient();
        client.addDefaultHeader("Authorization", "Bearer " + apiKey);
        conversations = new ConversationsApi(client);
        conversation = conversations.conversationsStartConversation();
        directLineMessageParser = new DirectLineMessageParser();
        startDIUser();
    }

    private void startDIUser(){
        startReceivingBotMessage();
        startReceivingQueueMessage();
    }

    private void startReceivingBotMessage(){
        if( receiverRemoteThread != null )
            return;

        receiverRemoteThread = new Thread(){
            @Override
            public void run() {
                String watermark = null;
                while(true) {
                    if (!receiveRemoteFlag)
                        break;
                    try {
                        ActivitySet activitySet = conversations.conversationsGetActivities(conversation.getConversationId(), watermark);
                        watermark = activitySet.getWatermark();
                        for(Activity a : activitySet.getActivities()){
                            if(!CHANNEL_ID.equals(a.getFrom().getId())){ // skip self messages
//                                System.out.println("Received from bot: " + a.getText());
                                HandOffGenericMessage genMsg = directLineMessageParser.parseMsg(a.getText());
                                MessageHandler.getMessageHandler().handleBotMessage(genMsg);
                            }
                        }
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
        receiveRemoteFlag = true;
        receiverRemoteThread.setName("DirectLine Receiver: " + this.getClass().getSimpleName());
        receiverRemoteThread.start();
    }

    public void stopConversation(){
        receiveRemoteFlag = false;
        try {
            receiverRemoteThread.join();
            receiverQueueThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            sendEndOfConversationMessage();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void startReceivingQueueMessage(){
        receiverQueueThread = new Thread() {
            @Override
            public void run(){
                while(true) {
                    if (!receiveQueueFlag) {
                        break;
                    }
                    try{
                        HandOffGenericMessage msg = MessageHandler.getMessageHandler().getBotMessage();
                        if(msg != null){
                            String jsonStringMsg = directLineMessageParser.parseHandoffMessage(msg);
                            sendMessage(jsonStringMsg);
                            if(msg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT){
                                // stopConversation();
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveQueueFlag = true;
        receiverQueueThread.setName("DirectLine Sender: " + this.getClass().getSimpleName());
        receiverQueueThread.start();
    }

    private void sendMessage(String message) throws ApiException {
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
