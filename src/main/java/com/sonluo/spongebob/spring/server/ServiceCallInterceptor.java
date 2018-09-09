package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author sunqian
 */
public interface ServiceCallInterceptor {

    int getOrder();

    void doIntercept(Request request, @Nullable Object result, Map<Object, Object> requestLocal);

    @Nullable
    default String getName() {
        return null;
    }
}
