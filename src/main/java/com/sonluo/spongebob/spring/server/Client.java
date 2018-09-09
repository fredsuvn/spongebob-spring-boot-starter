package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;

public interface Client extends ReadOnlyProperties {

    String getRemoteAddress();

    String getProtocol();

    @Nullable
    String getVersion();

    @Nullable
    String getDescriptor();
}
