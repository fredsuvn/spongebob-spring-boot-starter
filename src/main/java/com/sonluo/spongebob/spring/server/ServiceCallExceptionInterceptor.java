package com.sonluo.spongebob.spring.server;

import java.util.Map;

public interface ServiceCallExceptionInterceptor {

    Object doIntercept(Request request, Throwable throwable, Map<Object, Object> requestLocal);
}
