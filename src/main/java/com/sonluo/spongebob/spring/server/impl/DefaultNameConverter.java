package com.sonluo.spongebob.spring.server.impl;

import com.sonluo.spongebob.spring.server.ApiServiceMapping;
import com.sonluo.spongebob.spring.server.NameConverter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class DefaultNameConverter implements NameConverter {

    @Override
    public String toServiceName(String beanName, @Nullable ApiServiceMapping apiServiceMapping) {
        if (apiServiceMapping == null) {
            return beanName;
        }
        if (StringUtils.isEmpty(apiServiceMapping.value())) {
            return beanName;
        }
        return apiServiceMapping.value();
    }

    @Override
    public String toMethodName(Method method, ApiServiceMapping apiServiceMapping) {
        if (StringUtils.isEmpty(apiServiceMapping.value())) {
            return method.getName();
        }
        return apiServiceMapping.value();
    }

    @Override
    public String join(String... names) {
        return StringUtils.join(names, ".");
    }
}
