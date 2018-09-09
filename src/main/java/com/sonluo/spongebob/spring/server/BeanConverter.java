package com.sonluo.spongebob.spring.server;

import java.lang.reflect.Type;

/**
 * @author sunqian
 */
public interface BeanConverter {

    <T> T convert(Object src, Type type);
}
