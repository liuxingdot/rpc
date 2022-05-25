package com.liuaojie.Socket.server;

import com.liuaojie.Socket.client.RpcRequest;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class RpcServer {
    Logger logger= LoggerFactory.getLogger(RpcServer.class);
    private ExecutorService threadPool;
    public RpcServer(){
        /**
         * int corePoolSize,
         * int maximumPoolSize,
         * long keepAliveTime,
         *  TimeUnit unit,
         *  BlockingQueue<Runnable> workQueue
         */
        BlockingQueue<Runnable> workQueue=new ArrayBlockingQueue<Runnable>(100);
        threadPool=new ThreadPoolExecutor(10,50,100, TimeUnit.MILLISECONDS,workQueue);
    }
    void register(Object server,int port){
        try(ServerSocket serverSocket=new ServerSocket(port)){
            Socket socket=null;
            logger.info("服务器正在启动....");
            while((socket=serverSocket.accept()) !=null){
                logger.info("来自客户端连接"+socket.getInetAddress());
                Socket finalSocket = socket;
                threadPool.execute(new Runnable() {

                 @SneakyThrows
                 @Override
                 public void run() {
                     ObjectOutputStream outputStream = new ObjectOutputStream(finalSocket.getOutputStream());
                     ObjectInputStream inputStream = new ObjectInputStream((finalSocket.getInputStream()));
                     RpcRequest rpcRequest = (RpcRequest) inputStream.readObject();
                     System.out.println(rpcRequest);
                     Method method=server.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getClazz());
                     Object invoke = method.invoke(server, rpcRequest.getParams());
                     RpcResponse rpcResponse=new RpcResponse();
                     //rpcResponse.setData(invoke);
                     outputStream.writeObject(rpcResponse.success(invoke));
                     outputStream.flush();
                 }
             });
            }
        }catch (Exception e){
            logger.info("连接存在错误!"+e);
        }
    }
}
