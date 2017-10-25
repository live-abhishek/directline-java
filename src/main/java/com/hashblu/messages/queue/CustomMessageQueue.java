package com.hashblu.messages.queue;

import java.util.LinkedList;

/**
 * Created by abhisheks on 25-Oct-17.
 */
public class CustomMessageQueue<T> implements IMessageQueue<T> {

    private LinkedList<T> queue;

    public CustomMessageQueue(){
        queue = new LinkedList<>();
    }

    @Override
    public void pushMsg(T msg) {
        queue.addLast(msg);
    }

    @Override
    public T getMsg() {
        return queue.getFirst();
    }
}
