package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author sunqian
 */
public interface ServiceMapping {

    @Nullable
    ServiceCall getServiceCall(String url);

    void init(Map<String, ServiceCall> serviceCalls);
}
