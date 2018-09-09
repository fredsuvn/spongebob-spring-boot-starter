package com.sonluo.spongebob.spring.server;

/**
 * @author sunqian
 */
public interface Session extends ReadOnlyProperties {

    String getRemoteAddress();

    Client getClient();

    boolean canPush();

    void push(Object message);
}