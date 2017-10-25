package com.hashblu.messages.queue;

import java.util.LinkedList;

/**
 * Created by abhisheks on 25-Oct-17.
 */
public interface IMessageQueue<T> {
    boolean pushMsg(T msg);
    T getMsg();
}
