package com.sonluo.spongebob.spring.server;

public interface Channel {

    String getId();

    boolean isOpen();

    boolean canPush();

    void push(Object message);

    void close();
}
