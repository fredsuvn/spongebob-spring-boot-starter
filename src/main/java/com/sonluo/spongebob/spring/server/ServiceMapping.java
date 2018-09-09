package com.sonluo.spongebob.spring.server;

import java.util.Map;

/**
 * @author sunqian
 */
public interface ServiceMapping {

    ServiceCall getServiceCall(String url);

    void addAll(Map<String, ServiceCall> serviceCalls);
}
