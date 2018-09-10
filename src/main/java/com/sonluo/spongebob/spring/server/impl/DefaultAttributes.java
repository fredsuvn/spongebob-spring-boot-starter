package com.sonluo.spongebob.spring.server.impl;

import com.sonluo.spongebob.spring.server.Attributes;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sunqian
 */
public class DefaultAttributes implements Attributes {

    private final Map<String, Object> attributes = new HashMap<>();

    @Nullable
    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object attribute) {
        attributes.put(name, attribute);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
