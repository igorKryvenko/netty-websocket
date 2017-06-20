package com.netty.server;

/**
 * Created by ikryvenko on 20.06.17.
 */
public interface Server {
    public void start();
    public void stop();
    public void restart();
}
