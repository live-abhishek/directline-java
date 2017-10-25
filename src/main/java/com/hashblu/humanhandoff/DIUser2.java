package com.hashblu.humanhandoff;

import com.hashblu.messages.HandOffGenericMessage;
import com.hashblu.messages.queue.IMessageQueue;
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
    private volatile boolean receiveRemoteFlag = false;
    private volatile boolean receiveQueueFlag = false;
    private Thread receiverRemoteThread;
    private Thread receiverQueueThread;

    private IMessageQueue<HandOffGenericMessage> agentQueue;
    private IMessageQueue<HandOffGenericMessage> botQueue;

    public DIUser2(IMessageQueue<HandOffGenericMessage> botQueue, IMessageQueue<HandOffGenericMessage> agentQueue) throws ApiException {
        client = new ApiClient();
        client.addDefaultHeader("Authorization", "Bearer " + apiKey);
        conversations = new ConversationsApi(client);
        conversation = conversations.conversationsStartConversation();
        this.botQueue = botQueue;
        this.agentQueue = agentQueue;
    }

    public void startReceivingBotMessage(){
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
                        activitySet.getActivities().forEach(a -> {
                            if(!a.getFrom().getId().equals("Live-Agent")){
                                System.out.println("Received from bot: " + a.getText());
                                botQueue.pushMsg(new HandOffGenericMessage(a.getText()));
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
        receiveRemoteFlag = true;
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
                        HandOffGenericMessage msg = agentQueue.getMsg();
                        if(msg != null){
                            sendMessage(msg.getMsg());
                            if(msg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT){
                                stopConversation();
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
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
