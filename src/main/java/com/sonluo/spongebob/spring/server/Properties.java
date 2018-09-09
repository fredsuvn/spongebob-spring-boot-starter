package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public interface Properties {

    @Nullable
    Object getProperty(String name);

    @Nullable
    <T> T getProperty(String name, Class<T> type);

    @Nullable
    <T> T getProperty(String name, Type type);
}
