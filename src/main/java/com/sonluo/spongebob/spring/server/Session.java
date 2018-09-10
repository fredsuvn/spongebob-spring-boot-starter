package com.sonluo.spongebob.spring.server;

/**
 * @author sunqian
 */
public interface Session extends Attributes {

    String getId();

//    String getRemoteAddress();

//    Client getClient();

    boolean canPush();

    void push(Object message);

    void close();

    void onDestroy(Runnable action);
}