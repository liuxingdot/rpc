package com.liuaojie.netty.Client;

import com.liuaojie.Socket.client.RpcRequest;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
