package com.liuaojie.Socket.client;

import com.liuaojie.Socket.server.RpcResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class RpcProxyClient implements InvocationHandler {
    private String host;
    private int port;

    RpcProxyClient(String host,int port){
        this.host=host;
        this.port=port;
    }

    public  <T>T getProxy(Class<T> clazz){
         return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest=RpcRequest.builder()
                .InterfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .clazz(method.getParameterTypes())
                .build();
       RpcClient rpcClient=new RpcClient();
       return ((RpcResponse)rpcClient.sendRequest(host,port,rpcRequest)).getData();
    }


}
