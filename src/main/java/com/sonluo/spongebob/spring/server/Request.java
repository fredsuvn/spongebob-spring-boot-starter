package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;

/**
 * @author sunqian
 */
public interface Request<T> extends Attributes {

    @Nullable
    String getId();

    String getProtocol();

    String getUrl();

    String getRemoteAddress();

    Client getClient();

    @Nullable
    T getContent();

    @Nullable
    default Session getSession() {
        return getSession(false);
    }

    @Nullable
    Session getSession(boolean create);
}