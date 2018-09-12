package com.sonluo.spongebob.spring.utils;

import com.sonluo.spongebob.spring.server.Channel;
import com.sonluo.spongebob.spring.server.Session;
import com.sonluo.spongebob.spring.server.impl.InvalidChannel;
import com.sonluo.spongebob.spring.server.impl.InvalidSession;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Map;

public class SessionUtils {

    public static Session proxySession(Session valid) {
        return new Session() {

            private volatile Session proxy = valid;

            @Override
            public synchronized String getId() {
                return proxy.getId();
            }

            @Override
            public synchronized long createTime() {
                return proxy.createTime();
            }

            @Override
            public synchronized String getProtocol() {
                return proxy.getProtocol();
            }

            @Override
            public synchronized long getLastAccessTime() {
                return proxy.getLastAccessTime();
            }

            @Override
            public synchronized long getLastActiveTime() {
                return proxy.getLastActiveTime();
            }

            @Override
            public synchronized boolean isOpen() {
                return proxy.isOpen();
            }

            @Override
            public synchronized void close() {
                proxy.close();
                proxy = InvalidSession.INSTANCE;
            }

            @Override
            public synchronized Channel getDefaultChannel() {
                return proxy.getDefaultChannel();
            }

            @Override
            public synchronized boolean containsChannel(String channelId) {
                return proxy.containsChannel(channelId);
            }

            @Override
            @Nullable
            public synchronized Channel getChannel(String channelId) {
                return proxy.getChannel(channelId);
            }

            @Override
            public synchronized Map<String, Channel> getAllChannels() {
                return proxy.getAllChannels();
            }

            @Override
            public synchronized Channel createNewChannel(String channelId) {
                return proxy.createNewChannel(channelId);
            }

            @Override
            public synchronized Channel getOrCreateChannel(String channelId) {
                return proxy.getOrCreateChannel(channelId);
            }

            @Override
            @Nullable
            public synchronized Object getAttribute(String name) {
                return proxy.getAttribute(name);
            }

            @Override
            @Nullable
            public synchronized <T> T getAttribute(String name, Class<T> type) {
                return proxy.getAttribute(name, type);
            }

            @Override
            @Nullable
            public synchronized <T> T getAttribute(String name, Type type) {
                return proxy.getAttribute(name, type);
            }

            @Override
            public synchronized void setAttribute(String name, Object attribute) {
                proxy.setAttribute(name, attribute);
            }

            @Override
            public synchronized void removeAttribute(String name) {
                proxy.removeAttribute(name);
            }

            @Override
            public synchronized Map<String, Object> getAttributes() {
                return proxy.getAttributes();
            }
        };
    }

    public static Channel proxyChannel(Channel valid) {
        return new Channel() {

            private volatile Channel proxy = valid;

            @Override
            public synchronized String getId() {
                return proxy.getId();
            }

            @Override
            public synchronized boolean isOpen() {
                return proxy.isOpen();
            }

            @Override
            public synchronized boolean canPush() {
                return proxy.canPush();
            }

            @Override
            public synchronized void push(Object message) {
                proxy.push(message);
            }

            @Override
            public synchronized void close() {
                proxy.close();
                proxy = InvalidChannel.INSTANCE;
            }
        };
    }
}
