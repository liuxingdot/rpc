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

public class RpcServerRegister {
    Logger logger= LoggerFactory.getLogger(RpcServerRegister.class);
    ExecutorService threadPool=null;
    private static ServiceRegistry serviceRegistry = null;
    private static final RequestHandle requestHandle = null;
    public RpcServerRegister(ServiceRegistry serviceRegistry) {
        this.serviceRegistry=serviceRegistry;
        BlockingQueue<Runnable> workQueue=new ArrayBlockingQueue<Runnable>(100);
        threadPool=new ThreadPoolExecutor(10,50,100, TimeUnit.MILLISECONDS,workQueue);
    }
    void start(int port) {
        try(ServerSocket serverSocket=new ServerSocket(port)) {
            Socket socket;
            logger.info("服务器启动中....");
            while( (socket =serverSocket.accept())!=null){
                logger.info("客户端{}成功连接到服务器端{}",socket.getInetAddress(),socket.getLocalAddress());
                Socket finalSocket = socket;
                threadPool.execute(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        try {
                            ObjectOutputStream outputStream = new ObjectOutputStream(finalSocket.getOutputStream());
                            ObjectInputStream inputStream = new ObjectInputStream((finalSocket.getInputStream()));
                            RpcRequest rpcRequest = (RpcRequest) inputStream.readObject();
                            String interfaceName = rpcRequest.getInterfaceName();
                            System.out.println("service"+interfaceName );
                            Object service = serviceRegistry.getService(interfaceName);
                            System.out.println("service"+service);
                            Method method=service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getClazz());
                            Object invoke = method.invoke(service, rpcRequest.getParams());
                            RpcResponse<Object> objectRpcResponse = new RpcResponse<>();
                            outputStream.writeObject(objectRpcResponse.success(invoke));
                            outputStream.flush();
                        }catch (Exception e){
                            logger.info("错误信息！",e);
                        }

                    }
                });
                threadPool.shutdown();
        }
        }catch (Exception e){
            logger.info("错误信息！",e);
        }
        }

}
