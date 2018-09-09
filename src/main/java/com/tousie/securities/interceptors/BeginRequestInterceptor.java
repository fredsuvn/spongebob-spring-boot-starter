package com.tousie.securities.interceptors;

import com.sonluo.spongebob.spring.server.Request;
import com.sonluo.spongebob.spring.server.ServiceCallInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Map;

@Component
public class BeginRequestInterceptor implements ServiceCallInterceptor {

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public void doIntercept(Request request, @Nullable Object result, Map<Object, Object> requestLocal) {
        requestLocal.put("request-time", System.currentTimeMillis());
        logger.info("Request url: {}.", request.getUrl());
    }
}
