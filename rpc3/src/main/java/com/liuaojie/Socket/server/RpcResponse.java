package com.liuaojie.Socket.server;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@Data
public class RpcResponse <T> implements Serializable {
    Logger logger= LoggerFactory.getLogger(RpcResponse.class);
    /**
     * 接收到响应状态码
     */
    private int statusCode;
    /**
     * 响应状态补充信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public RpcResponse success(T data){
        logger.info("信息获取正确!");
        RpcResponse rpcResponse=new RpcResponse();
        rpcResponse.setStatusCode(200);
        rpcResponse.setData(data);
        return rpcResponse;
    }
}
