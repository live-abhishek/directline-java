package com.hashblu.humanhandoff;

import com.hashblu.MessageHandler;
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
        ConfigurableApplicationContext ctx = SpringApplication.run(MainRunner.class, args);
        ctx.getBean(MainRunner.class).startApp();
    }

    public void startApp() throws ApiException {
        MessageHandler.getMessageHandler();
        new DIUser2();
    }
}
