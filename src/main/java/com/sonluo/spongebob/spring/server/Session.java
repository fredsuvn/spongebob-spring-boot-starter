package com.sonluo.spongebob.spring.server;

import java.util.Collection;

/**
 * @author sunqian
 */
public interface Session extends Attributes {

    String getId();

    String getProtocol();

    long getLastAccessTime();

    long getLastActiveTime();

    boolean isAlive();

    void close();

    Channel getDefaultChannel();

    Channel getChannel(String channelId);

    Collection<Channel> getAllChannels();

    Channel createNewChannel(String channelId);
}