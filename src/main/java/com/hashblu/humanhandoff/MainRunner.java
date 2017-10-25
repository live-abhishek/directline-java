package com.hashblu.humanhandoff;

import com.hashblu.agents.AgentClientRunner;
import com.hashblu.agents.LiveChatAgentClient;
import com.hashblu.messages.HandOffGenericMessage;
import com.hashblu.messages.queue.CustomMessageQueue;
import com.hashblu.messages.queue.IMessageQueue;
import io.swagger.client.ApiException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by abhisheks on 25-Oct-17.
 */
@SpringBootApplication
public class MainRunner {
    public static void main(String[] args) throws ApiException {
        ConfigurableApplicationContext ctx = SpringApplication.run(LiveChatRunner.class, args);
        ctx.getBean(LiveChatRunner.class).startApp();
    }

    public void startApp() throws ApiException {
        IMessageQueue<HandOffGenericMessage> botQueue = new CustomMessageQueue<HandOffGenericMessage>();
        IMessageQueue<HandOffGenericMessage> agentQueue = new CustomMessageQueue<HandOffGenericMessage>();
        AgentClientRunner client = new AgentClientRunner(new LiveChatAgentClient(AppConstants.LIVE_CHAT_LICENCE_ID), agentQueue, botQueue);
    }
}
