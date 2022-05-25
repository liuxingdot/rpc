package com.liuaojie.Socket.server;

public class TestServer {
    public static void main(String[] args) {
        HelloServer helloServer=new HelloServerImpl();
        RpcServer rpcServer=new RpcServer();
        rpcServer.register(helloServer,6666);
    }
}
