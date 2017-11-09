package com.hashblu.exceptions;

/**
 * Created by abhisheks on 09-Nov-17.
 */
public class AgentMessageRetrievalException extends RuntimeException {
    public AgentMessageRetrievalException(String msg){
        super(msg);
    }

    public AgentMessageRetrievalException(Throwable cause){
        this("Unknown Reason", cause);
    }

    public AgentMessageRetrievalException(String msg, Throwable cause){
        super(msg, cause);
    }
}
