package com.sonluo.spongebob.spring.server;

import com.sonluo.spongebob.spring.server.impl.DefaultBeanOperator;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Map;

public interface Attributes {

    @Nullable
    Object getAttribute(Object key);

    @Nullable
    default <T> T getAttribute(Object key, Class<T> type) {
        Object src = getAttribute(key);
        if (src == null) {
            return null;
        }
        return DefaultBeanOperator.INSTANCE.convert(src, type);
    }

    @Nullable
    default <T> T getAttribute(Object key, Type type) {
        Object src = getAttribute(key);
        if (src == null) {
            return null;
        }
        return DefaultBeanOperator.INSTANCE.convert(src, type);
    }

    void setAttribute(Object key, Object attribute);

    void removeAttribute(Object key);

    Map<Object, Object> getAttributes();
}
