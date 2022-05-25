package com.liuaojie.Socket.server;

import java.io.IOException;

public class TestServer1 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        HelloServer helloServer=new HelloServerImpl();
        ServiceRegistry serviceRegistry=new ServiceRegistryImpl();
        serviceRegistry.register(helloServer);
        RpcServerRegister rpcServerRegister=new RpcServerRegister(serviceRegistry);
        rpcServerRegister.start(6667);
    }
}
