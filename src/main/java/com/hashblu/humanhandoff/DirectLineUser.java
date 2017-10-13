package com.hashblu.humanhandoff;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ConversationsApi;
import io.swagger.client.model.*;

/**
 * Created by abhisheks on 27-Sep-17.
 */
public class DirectLineUser {

    public static void main(String... args) throws ApiException {
        String apiKey = "9-fc_dszchY.cwA.FSI.afBGSms6txE6FzWHjXvz8wapsj4jbPBC5Mgs_u9Bmy0";

        ConversationsApi conversationsApi = new ConversationsApi();

        ApiClient client = conversationsApi.getApiClient();
        client.addDefaultHeader("Authorization", "Bearer " + apiKey);
        Conversation conversation = conversationsApi.conversationsStartConversation();

        Activity activity = new Activity();
        ChannelAccount channelAccount = new ChannelAccount();
        channelAccount.setName("user1");
        channelAccount.setId("directline");
        activity.setFrom(channelAccount);
        activity.setType("Message");
        activity.setText("hello my bot!");
        ResourceResponse response = conversationsApi.conversationsPostActivity(conversation.getConversationId(), activity);

        System.out.println("@@get conversation messages");
        String watermark = "";
        do {
            ActivitySet activitySet = //
                    conversationsApi.conversationsGetActivities(conversation.getConversationId(), watermark);
            System.out.println("@@activitySet size = " + activitySet.getActivities().size());
            for (Activity act : activitySet.getActivities()) {
                System.out.println("\t" + act.getFrom().getName() + " says \"" + act.getText() + "\"");
            }
            if (activitySet.getWatermark() == null || watermark.equals(activitySet.getWatermark()) == false)
                break;
            watermark = activitySet.getWatermark();
            System.out.println("\twatermark = " + watermark);
        } while (true);
    }
}
