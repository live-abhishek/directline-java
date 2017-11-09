package com.hashblu.agents;

import com.hashblu.humanhandoff.AppConstants;

/**
 * Created by abhisheks on 09-Nov-17.
 */
public class AgentClientProvider {
    public static IAgentClient getAgentClient(){
        IAgentClient agentClient = null;
        String clientName = AppConstants.AGENT_CLIENT_TYPE;
        if("salesforce".equals(clientName)){
            agentClient = new SalesforceAgentClient(AppConstants.SALESFORCE_ORGID, AppConstants.SALESFORCE_DEPLOYMENT_ID, AppConstants.SALESFORCE_CHATBUTTON_ID, AppConstants.SALESFORCE_API_VERSION, AppConstants.SALESFORCE_VISITOR_NAME);
        } else if ("livechat".equals((clientName))){
            agentClient = new LiveChatAgentClient(AppConstants.LIVE_CHAT_LICENCE_ID);
        }
        return agentClient;
    }
}
