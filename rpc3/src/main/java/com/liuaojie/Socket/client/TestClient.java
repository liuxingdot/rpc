package com.liuaojie.Socket.client;

import com.liuaojie.Socket.server.HelloServer;

public class TestClient {
    public static void main(String[] args) {
        RpcProxyClient rpcProxyClient=new RpcProxyClient("127.0.0.1",6666);
        HelloServer helloServer = rpcProxyClient.getProxy(HelloServer.class);
        HelloObject helloObject=new HelloObject(12,"你好！");
        String hello = helloServer.hello(helloObject);
        System.out.println(hello);

    }
}
