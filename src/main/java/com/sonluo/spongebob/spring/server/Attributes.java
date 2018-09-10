package com.sonluo.spongebob.spring.server;

import com.sonluo.spongebob.spring.server.impl.DefaultBeanOperator;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Map;

public interface Attributes {

    @Nullable
    Object getAttribute(String name);

    @Nullable
    default <T> T getAttribute(String name, Class<T> type) {
        Object src = getAttribute(name);
        if (src == null) {
            return null;
        }
        return DefaultBeanOperator.INSTANCE.convert(src, type);
    }

    @Nullable
    default <T> T getAttribute(String name, Type type) {
        Object src = getAttribute(name);
        if (src == null) {
            return null;
        }
        return DefaultBeanOperator.INSTANCE.convert(src, type);
    }

    void setAttribute(String name, Object attribute);

    void removeAttribute(String name);

    Map<String, Object> getAttributes();
}
