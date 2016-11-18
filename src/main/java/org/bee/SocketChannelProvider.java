package org.bee;


import java.nio.channels.SocketChannel;

public interface SocketChannelProvider {
    /**
     * Provides socket channel to init restore connection.
     * You could change hosts on fail and sleep between retries in this method
     * @param retryNumber number of current retry. -1 on initial connect.
     * @param lastError   the last error occurs when reconnecting
     * @return the result of SocketChannel open(SocketAddress remote) call
     */
    SocketChannel get(int retryNumber, Throwable lastError);
}
