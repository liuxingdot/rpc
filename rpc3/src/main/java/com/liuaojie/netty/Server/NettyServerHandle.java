package com.liuaojie.netty.Server;

import com.liuaojie.Socket.server.HelloServer;
import com.liuaojie.Socket.server.RpcResponse;
import com.liuaojie.Socket.server.ServiceRegistry;
import com.liuaojie.Socket.server.ServiceRegistryImpl;
import com.liuaojie.Socket.client.RpcRequest;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

class NettyServerHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    //private static RequestHandler requestHandler;
    private static final ServiceRegistry serviceRegistry;

    static {
        //requestHandler = new RequestHandler();
        serviceRegistry = new ServiceRegistryImpl();
    }
/*

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }
*/

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if(o instanceof String) logger.info("我被反序列化成字符串了");
        if(o instanceof RpcRequest)  logger.info("我是RpcRequest对象");
        RpcRequest rpcRequest = (RpcRequest) o;
        logger.info("服务器端接收到消息位{}", rpcRequest);
        String interfaceName = rpcRequest.getInterfaceName();
        Object service = serviceRegistry.getService(interfaceName);
        System.out.println("service"+service);
        Method method=service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getClazz());
        Object invoke = method.invoke(service, rpcRequest.getParams());
        /**
         * Method method=server.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getClazz());
         *                      Object invoke = method.invoke(server, rpcRequest.getParams());
         */
        RpcResponse rpcResponse = new RpcResponse();
        ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(rpcResponse.success(invoke));
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }
}
