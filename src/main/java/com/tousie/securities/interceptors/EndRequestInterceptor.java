package com.tousie.securities.interceptors;

import com.sonluo.spongebob.spring.server.Request;
import com.sonluo.spongebob.spring.server.ServiceCallInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Map;

@Component
public class EndRequestInterceptor implements ServiceCallInterceptor {
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void doIntercept(Request request, @Nullable Object result, Map<Object, Object> requestLocal) {
        long now = System.currentTimeMillis();
        long begin = (Long) requestLocal.get("request-time");
        logger.info("End request {}, cost {} ms.", request.getUrl(), (now - begin));
    }
}
