package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;

/**
 * @author sunqian
 */
public interface Request<T> {

    String getUrl();

    String getRemoteAddress();

    T getContent();

    Client getClient();

    @Nullable
    default Session getSession() {
        return getSession(false);
    }

    @Nullable
    Session getSession(boolean create);
}