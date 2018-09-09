package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;

/**
 * @author sunqian
 */
public interface ServiceCall {

    @Nullable
    Object doService(Request request) throws ServiceNotFoundException;
}
