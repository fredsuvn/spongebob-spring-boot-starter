package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * @author sunqian
 */
public interface NameConverter {

    String toServiceName(String beanName, @Nullable ApiServiceMapping apiServiceMapping);

    String toMethodName(Method method, ApiServiceMapping apiServiceMapping);
}
