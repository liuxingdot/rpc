package com.liuaojie.netty.reflect;

import com.liuaojie.Socket.client.RpcRequest;
import com.liuaojie.netty.Client.RpcClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Data
public
class ProxyNettyClient implements InvocationHandler {
    private RpcClient rpcClient;

    public ProxyNettyClient(RpcClient rpcClient){
        this.rpcClient=rpcClient;
    }
    public  <T>T getProxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
       RpcRequest rpcRequest=RpcRequest.builder()
               .InterfaceName(method.getDeclaringClass().getName())
               .methodName(method.getName())
               .clazz(method.getParameterTypes())
               .params(args)
               .build();
        return rpcClient.sendRequest(rpcRequest);
    }
}
