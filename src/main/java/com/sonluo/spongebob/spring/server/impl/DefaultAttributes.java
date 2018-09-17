package com.sonluo.spongebob.spring.server.impl;

import com.sonluo.spongebob.spring.server.Attributes;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunqian
 */
public class DefaultAttributes implements Attributes {

    private final Map<Object, Object> attributes = new ConcurrentHashMap<>();

    @Nullable
    @Override
    public Object getAttribute(Object key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(Object key, Object attribute) {
        attributes.put(key, attribute);
    }

    @Override
    public void removeAttribute(Object key) {
        attributes.remove(key);
    }

    @Override
    public Map<Object, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
