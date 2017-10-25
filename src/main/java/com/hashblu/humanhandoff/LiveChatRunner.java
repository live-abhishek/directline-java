package com.hashblu.humanhandoff;

import com.hashblu.messages.messageListener.ConsolePrinterMessageListener;
import com.hashblu.agents.AgentClientRunner;
import com.hashblu.agents.LiveChatAgentClient;
import io.swagger.client.ApiException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

/**
 * Created by abhisheks on 23-Oct-17.
 */
//@SpringBootApplication
public class LiveChatRunner {
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ApiException {
        ConfigurableApplicationContext ctx = SpringApplication.run(LiveChatRunner.class, args);
        ctx.getBean(LiveChatRunner.class).startApp();
    }

    public void startApp() throws ApiException {
        AgentClientRunner client = new AgentClientRunner(new LiveChatAgentClient(AppConstants.LIVE_CHAT_LICENCE_ID), new ConsolePrinterMessageListener());
        String msg = "";
        client.startHandoff();
        while(true){
            msg = scanner.nextLine();
            if(msg.toLowerCase().equals("stop")){
                client.endChat();
                break;
            }
//            System.out.println("echo: " + msg);
//            client.sendChat(msg);
        }
    }
}
