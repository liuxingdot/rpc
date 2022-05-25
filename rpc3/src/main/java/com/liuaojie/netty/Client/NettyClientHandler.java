package com.liuaojie.netty.Client;

import com.liuaojie.Socket.server.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler {

        private final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

      /*  @Override
        protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
            try {
                logger.info(String.format("客户端接收到消息: %s", msg));
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                ctx.channel().attr(key).set(msg);
                ctx.channel().close();
            } finally {

            }
        }*/

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("过程调用时有错误发生:");
            cause.printStackTrace();
            ctx.close();
        }
/*
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }*/

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        try {
            RpcResponse msg=(RpcResponse)o;
            logger.info(String.format("客户端接收到消息: %s", msg));
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            channelHandlerContext.channel().attr(key).set(msg);
            channelHandlerContext.channel().close();
        }
        finally {
            ReferenceCountUtil.release(o);
        }
    }
}
