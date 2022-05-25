package com.liuaojie.Socket.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcClient {
    Logger logger= LoggerFactory.getLogger(RpcClient.class);
    public Object sendRequest(String host,int post,RpcRequest rpcRequest){
        try(Socket socket=new Socket(host,post)){
            ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        }catch (Exception e){
            logger.info("请求错误信息",e);
            return null;
        }
    }
}
