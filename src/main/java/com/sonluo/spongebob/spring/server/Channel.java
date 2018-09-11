package com.sonluo.spongebob.spring.server;

public interface Channel {

    String getId();

    long createTime();

    boolean canPush();

    void push(Object message);
}
