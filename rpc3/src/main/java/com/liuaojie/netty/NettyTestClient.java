package com.liuaojie.netty;

import com.liuaojie.Socket.client.HelloObject;
import com.liuaojie.Socket.client.RpcProxyClient;
import com.liuaojie.Socket.server.HelloServer;
import com.liuaojie.Socket.server.HelloServerImpl;
import com.liuaojie.netty.Client.RpcClient;
import com.liuaojie.netty.Client.RpcClientImpl;
import com.liuaojie.netty.reflect.ProxyNettyClient;

public class NettyTestClient {
    public static void main(String[] args) {

        RpcClient rpcClient=new RpcClientImpl("127.0.0.1",7777);
        //System.out.println(rpcClient);
        //HelloServer helloServer=new HelloServerImpl();
        ProxyNettyClient proxyNettyClient=new ProxyNettyClient(rpcClient);
        HelloServer proxy = proxyNettyClient.getProxy(HelloServer.class);
        HelloObject helloObject=new HelloObject(12,"这是一条年龄信息！");
        String hello = proxy.hello(helloObject);
        System.out.println(hello);

    }
}
