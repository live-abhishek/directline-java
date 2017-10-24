package com.hashblu;

import com.hashblu.agents.HandOffGenericMessage;

/**
 * Created by abhisheks on 23-Oct-17.
 */
public class ConsolePrinterMessageListener implements IReceiveMessageListener {

    @Override
    public void processMessage(String msg) {
        System.out.println(msg);
    }

    public void processMessage(HandOffGenericMessage msg){
        if(msg.getMsgType() == HandOffGenericMessage.MessageType.CHAT_END_FROM_AGENT){
            processMessage("Chat ended by Agent");
        } else if(msg.getMsg() != null && !msg.getMsg().isEmpty()) {
            processMessage(msg.getMsg());
        }
    }
}
