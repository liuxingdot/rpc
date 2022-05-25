package com.liuaojie.netty;

import com.liuaojie.Socket.server.*;
import com.liuaojie.netty.Server.RpcServer;
import com.liuaojie.netty.Server.RpcServerImpl;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloServer helloServer=new HelloServerImpl();
        ServiceRegistry registry=new ServiceRegistryImpl();
        registry.register(helloServer);
        System.out.println(registry);
        RpcServer rpcServer=new RpcServerImpl();
        rpcServer.start(7777);
    }
}
