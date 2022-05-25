package com.liuaojie.netty.Client;

import com.liuaojie.Socket.server.RpcResponse;
import com.liuaojie.Socket.client.RpcRequest;
import com.liuaojie.netty.rpc_Decoder.CommonDecoder;
import com.liuaojie.netty.rpc_Encoder.CommonEncoder;
import com.liuaojie.netty.rpc_serializer.JsonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClientImpl implements RpcClient {
    public RpcClientImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private String host;
    private int port;
    private static final Bootstrap bootstrap;
    Logger logger= LoggerFactory.getLogger(RpcClientImpl.class);

    static {
        EventLoopGroup group=new NioEventLoopGroup();

        bootstrap=new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new CommonDecoder());
                        pipeline.addLast(new CommonEncoder(new JsonSerializer()));
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try{
            ChannelFuture future = bootstrap.connect(host,port).sync();
            logger.info("正在连接服务器:{}:{}",host,port);
            Channel channel = future.channel();
            if(channel!=null){
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()){
                        logger.info(String.format("客户端发送消息:%s",rpcRequest.toString()));
                    }else{
                        logger.info("客户端发送消息有错误发生",future1.cause());
                    }
                });
            }
            channel.closeFuture().sync();
            AttributeKey<RpcResponse>  key=AttributeKey.valueOf("RpcResponse");
            RpcResponse rpcResponse = channel.attr(key).get();
            return rpcResponse.getData();
        }catch (Exception e){
           logger.info("连接时有错误发生！",e);
        }
        return null;
    }
}
