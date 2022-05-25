package com.liuaojie.netty.rpc_Decoder;

import com.liuaojie.Socket.server.RpcResponse;
import com.liuaojie.Socket.client.RpcRequest;
import com.liuaojie.netty.rpc_serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommonDecoder extends ByteToMessageDecoder {
    Logger logger= LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER=0xCAFEBABE;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println(MAGIC_NUMBER);
         int magic=byteBuf.readInt();
         if(magic!=MAGIC_NUMBER){
             logger.error("不识别的协议包: {}", magic);
             //throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
             return;
         }
        int packageCode = byteBuf.readInt();
        Class<?> packageClass;
        if(packageCode == 1) {
            packageClass = RpcRequest.class;
        } else if(packageCode == 0) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别的数据包: {}", packageCode);
           // throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
            return;
        }
        int serialerCode=byteBuf.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serialerCode);
        if(serializer == null) {
            logger.error("不识别的反序列化器: {}", 1);
            //throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
            return;
        }
        int len=byteBuf.readInt();
        byte[] bytes=new byte[len];
        byteBuf.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);
        list.add(obj);
    }
}
