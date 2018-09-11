package com.sonluo.spongebob.spring.server.impl;

import com.sonluo.spongebob.spring.server.Channel;
import com.sonluo.spongebob.spring.server.Session;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public class InvalidSession implements Session {

    public static final InvalidSession INSTANCE = new InvalidSession();

    private static final String ILLEGAL_STATE_MESSAGE = "This session is invalid!";

    @Override
    public String getId() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public long createTime() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public String getProtocol() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public long getLastAccessTime() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public long getLastActiveTime() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public void close() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public Channel getDefaultChannel() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public boolean containsChannel(String channelId) {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Nullable
    @Override
    public Channel getChannel(String channelId) {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public Collection<Channel> getAllChannels() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public Channel createNewChannel(String channelId) {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Nullable
    @Override
    public Object getAttribute(String name) {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public void setAttribute(String name, Object attribute) {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public void removeAttribute(String name) {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }

    @Override
    public Map<String, Object> getAttributes() {
        throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
    }
}
