package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author sunqian
 */
public interface Session extends Attributes {

    String getId();

    long createTime();

    String getProtocol();

    long getLastAccessTime();

    long getLastActiveTime();

    boolean isAlive();

    void beat();

    void close();

    Channel getDefaultChannel();

    boolean containsChannel(String channelId);

    @Nullable
    Channel getChannel(String channelId);

    Collection<Channel> getAllChannels();

    Channel createNewChannel(String channelId);
}