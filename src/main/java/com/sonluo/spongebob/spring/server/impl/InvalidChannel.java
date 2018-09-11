package com.sonluo.spongebob.spring.server.impl;

import com.sonluo.spongebob.spring.server.Channel;

public class InvalidChannel implements Channel {

    public static final InvalidChannel INSTANCE = new InvalidChannel();

    private static final String ILLEGAL_STATE_MESSAGE = "This channel is invalid!";

    @Override
    public String getId() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean canPush() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public void push(Object message) {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public void close() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }
}
