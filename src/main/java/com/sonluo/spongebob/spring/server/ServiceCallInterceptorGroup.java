package com.sonluo.spongebob.spring.server;

import java.util.List;

public interface ServiceCallInterceptorGroup {

    String getName();

    List<ServiceCallInterceptor> getInterceptors();
}
