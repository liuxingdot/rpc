package com.liuaojie.netty.rpc_Encoder;

import com.liuaojie.Socket.client.RpcRequest;
import com.liuaojie.netty.rpc_serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * +---------------+---------------+-----------------+-------------+
 * |  Magic Number |  Package Type | Serializer Type | Data Length |
 * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
 * +---------------+---------------+-----------------+-------------+
 * |                          Data Bytes                           |
 * |                   Length: ${Data Length}                      |
 * +---------------------------------------------------------------+
 */
public class CommonEncoder extends MessageToByteEncoder {
    private static final int MAGIC_NUMBER=0xCAFEBABE;
    private final CommonSerializer serializer;


    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
          byteBuf.writeInt(MAGIC_NUMBER);
          if(o instanceof RpcRequest){
              byteBuf.writeInt(1);
          }else{
              byteBuf.writeInt(0);
          }
          byteBuf.writeInt(serializer.getCode());
          byte[] bytes=serializer.serialize(o);
          byteBuf.writeInt(bytes.length);
          byteBuf.writeBytes(bytes);
    }
}
