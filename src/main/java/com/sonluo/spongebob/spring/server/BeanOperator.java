package com.sonluo.spongebob.spring.server;

import java.lang.reflect.Type;

/**
 * @author sunqian
 */
public interface BeanOperator {

    default <T> T convert(Object src, Class<T> type) {
        return convert(src, (Type) type);
    }

    <T> T convert(Object src, Type type);

    Object getProperty(Object obj, String name);

    default <T> T getProperty(Object obj, String name, Class<T> type) {
        return getProperty(obj, name, (Type) type);
    }

    default <T> T getProperty(Object obj, String name, Type type) {
        Object result = getProperty(obj, name);
        return convert(result, type);
    }

    void setProperty(Object obj, String name, Object property);
}
