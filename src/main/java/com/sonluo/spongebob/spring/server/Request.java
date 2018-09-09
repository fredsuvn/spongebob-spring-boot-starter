package com.sonluo.spongebob.spring.server;

/**
 * @author sunqian
 */
public interface Request<T> {

    String getUrl();

    String getRemoteAddress();

    T getContent();

    Client getClient();

    Session getSession();
}