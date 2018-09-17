package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

/**
 * @author sunqian
 */
public interface BeanOperator {

    default <T> T convert(Object src, Class<T> type) {
        return convert(src, (Type) type);
    }

    <T> T convert(Object src, Type type);

    @Nullable
    Object getProperty(Object obj, String name);

    @Nullable
    default <T> T getProperty(Object obj, String name, Class<T> type) {
        return getProperty(obj, name, (Type) type);
    }

    @Nullable
    default <T> T getProperty(Object obj, String name, Type type) {
        Object result = getProperty(obj, name);
        if (result == null) {
            return null;
        }
        return convert(result, type);
    }

    void setProperty(Object obj, String name, Object property);

    boolean isBasicType(Class type);
}
