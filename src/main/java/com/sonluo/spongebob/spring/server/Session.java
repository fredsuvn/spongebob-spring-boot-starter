package com.sonluo.spongebob.spring.server;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author sunqian
 */
public interface Session extends Attributes {

    String getId();

    long createTime();

    String getProtocol();

    long getLastAccessTime();

    long getLastActiveTime();

    boolean isOpen();

    void close();

    Channel getDefaultChannel();

    boolean containsChannel(String channelId);

    @Nullable
    Channel getChannel(String channelId);

    Map<String, Channel> getAllChannels();

    Channel createNewChannel(String channelId);

    default Channel getOrCreateChannel(String channelId) {
        Channel channel = getChannel(channelId);
        if (channel != null) {
            return channel;
        }
        return createNewChannel(channelId);
    }
}