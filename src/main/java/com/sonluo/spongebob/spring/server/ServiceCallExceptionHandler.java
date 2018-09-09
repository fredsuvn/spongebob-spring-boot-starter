package com.sonluo.spongebob.spring.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public interface ServiceCallExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ServiceCallInterceptor.class);

    Object handle(Request request, Throwable throwable, Map<Object, Object> requestLocal);
}
