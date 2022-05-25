package com.liuaojie.Socket.server;

public interface ServiceRegistry<T> {
    void register(T service);
    Object getService(String ServiceName);
}
