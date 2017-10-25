package com.hashblu.humanhandoff;

import io.swagger.client.ApiException;

import java.util.Scanner;

/**
 * Created by abhisheks on 05-Oct-17.
 */
public class DLRunner {
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String... args) throws ApiException {
        DIUser2 dlUser = new DIUser2();
        System.out.println("Enter the message you want to send!");
        String msg = "";
//        dlUser.startReceivingMessage();
        while(true){
            msg = scanner.nextLine();
            if(msg.toLowerCase().equals("stop")){
                dlUser.stopConversation();
                break;
            }
//            System.out.println("echo: " + msg);
//            dlUser.sendMessage(msg);
        }
    }
}
