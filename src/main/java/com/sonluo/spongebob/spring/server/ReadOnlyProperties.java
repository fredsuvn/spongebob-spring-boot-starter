package com.sonluo.spongebob.spring.server;

import com.sonluo.spongebob.spring.server.impl.DefaultBeanConverter;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Map;

public interface ReadOnlyProperties {

    @Nullable
    Object getProperty(String name);

    @Nullable
    default <T> T getProperty(String name, Class<T> type) {
        Object src = getProperty(name);
        if (src == null) {
            return null;
        }
        return DefaultBeanConverter.INSTANCE.convert(src, type);
    }

    @Nullable
    default <T> T getProperty(String name, Type type) {
        Object src = getProperty(name);
        if (src == null) {
            return null;
        }
        return DefaultBeanConverter.INSTANCE.convert(src, type);
    }

    Map<String, Object> asMap();
}
