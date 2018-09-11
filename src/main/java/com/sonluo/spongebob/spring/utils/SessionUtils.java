package com.sonluo.spongebob.spring.utils;

import com.sonluo.spongebob.spring.server.Channel;
import com.sonluo.spongebob.spring.server.Session;
import com.sonluo.spongebob.spring.server.impl.InvalidChannel;

public class SessionUtils {

    public static Session proxySession(Session valid) {
//        return new
        return null;
    }

    public static Channel proxyChannel(Channel valid) {
        return new Channel() {

            private volatile Channel proxy = valid;

            @Override
            public String getId() {
                return proxy.getId();
            }

            @Override
            public boolean isOpen() {
                boolean result = proxy.isOpen();
                if (!result) {
                    proxy = InvalidChannel.INSTANCE;
                }
                return result;
            }

            @Override
            public boolean canPush() {
                return proxy.canPush();
            }

            @Override
            public void push(Object message) {
                proxy.push(message);
            }

            @Override
            public void close() {
                proxy.close();
                proxy = InvalidChannel.INSTANCE;
            }
        };
    }
}
