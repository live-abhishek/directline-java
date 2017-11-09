package com.hashblu.exceptions;

/**
 * Created by abhisheks on 09-Nov-17.
 */
public class AgentConnectionException extends RuntimeException {
    public AgentConnectionException(String msg){
        super(msg);
    }

    public AgentConnectionException(Throwable cause){
        this("Unknown Reason", cause);
    }

    public AgentConnectionException(String msg, Throwable cause){
        super(msg, cause);
    }
}
