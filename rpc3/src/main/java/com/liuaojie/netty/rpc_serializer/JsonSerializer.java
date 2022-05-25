package com.liuaojie.netty.rpc_serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuaojie.Socket.client.RpcRequest;
import com.liuaojie.netty.rpc_Decoder.CommonDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonSerializer implements CommonSerializer {
    Logger logger= LoggerFactory.getLogger(CommonDecoder.class);
    private ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public byte[] serialize(Object obj) {
        try{
            return objectMapper.writeValueAsBytes(obj);
        }catch (JsonProcessingException e){
            logger.error("序列化时有错误发生: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
        //return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try{
            Object obj=objectMapper.readValue(bytes,clazz);
            if(obj instanceof RpcRequest) {
                obj = handleRequest(obj);
            }
            return obj;
        }catch (IOException e){
            logger.error("反序列化时有错误发生: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for(int i = 0; i < rpcRequest.getClazz().length; i ++) {
            Class<?> clazz = rpcRequest.getClazz()[i];
            if(!clazz.isAssignableFrom(rpcRequest.getClazz()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getClazz()[i]);
                rpcRequest.getClazz()[i] = (Class) objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }

    @Override
    public int getCode() {
        //return SerializerCode.valueOf("JSON").getCode();
        return 1;
    }
}
