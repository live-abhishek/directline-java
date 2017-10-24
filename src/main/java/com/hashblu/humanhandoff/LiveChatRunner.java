package com.hashblu.humanhandoff;

import com.hashblu.ConsolePrinterMessageListener;
import com.hashblu.agents.AgentClient;
import com.hashblu.agents.LiveChatAgentClient;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

/**
 * Created by abhisheks on 23-Oct-17.
 */
@SpringBootApplication
public class LiveChatRunner {
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ApiException {
        ConfigurableApplicationContext ctx = SpringApplication.run(LiveChatRunner.class, args);
        ctx.getBean(LiveChatRunner.class).startApp();
    }

    public void startApp() throws ApiException {
        AgentClient client = new AgentClient(new LiveChatAgentClient(AppConstants.LIVE_CHAT_LICENCE_ID), new ConsolePrinterMessageListener());
        System.out.println("Enter the message you want to send!");
        String msg = "";
        client.startReceivingMessage();
        while(true){
            msg = scanner.nextLine();
            if(msg.toLowerCase().equals("stop")){
                client.endChat();
                break;
            }
//            System.out.println("echo: " + msg);
            client.sendChat(msg);
        }
    }
}
