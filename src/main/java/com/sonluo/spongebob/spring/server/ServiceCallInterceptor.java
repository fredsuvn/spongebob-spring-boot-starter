package com.sonluo.spongebob.spring.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author sunqian
 */
public interface ServiceCallInterceptor {

    Logger logger = LoggerFactory.getLogger(ServiceCallInterceptor.class);

    int getOrder();

    void doIntercept(Request request, @Nullable Object result, Map<Object, Object> requestLocal);

    @Nullable
    default String getName() {
        return null;
    }
}
