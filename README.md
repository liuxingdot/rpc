# Rpc

## 系统介绍
本系统主要实现了客户端对服务器端接口函数调用，底层网络通信分别实现了Socket和Netty两种方式，同时自定义双方的通信协议，以及客户端同JDK动态代理自动实现接口调用等方案

##
## 项目优化方向

1.当大量请求并发访问服务器时候，我们经量能够充分利用服务器性能，可以采用负载均衡技术，比如随机算法，轮询算法，以及一致性哈希算法等实现服务器端调用

2.网络通信时候，由于发送端数据过大超出了发送缓冲区的长度，或者接收缓冲区剩余空间少于发送端数据包，因此需要拆包处理，另外，当接收端未及时取出缓冲区数据，因此发生粘包，所以对数据包添加长度，合理解决粘包和拆包问题

## 实现技术点
### 1. 使用Socket和Netty两种实现网络通信
1）利用Socket进程间通信方式，绑定相关的端口，通过SocketChannel实现进程间通信，服务器利用了线程池，实现了每一个socket事件对应一个线程处理
2）利用Netty方式通信，实现高效率传输

##
### 2. 自定义协议以及序列化接口
在传输过程中，我们可以在发送的数据上加上各种必要的数据，形成自定义的协议，而自动加上这个数据就是编码器的工作，解析数据获得原始数据就是解码器的工作。
##
协议格式：
+---------------+---------------+-----------------+-------------+
|  Magic Number |  Package Type | Serializer Type | Data Length |
|    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
+---------------+---------------+-----------------+-------------+
|                          Data Bytes                           |
|                   Length: ${Data Length}                      |
+---------------------------------------------------------------+
##
序列化和反序列化都使用的JSON
序列化将对象翻译成字节数组，反序列化将字节数组和对象翻译成对象 


### 3. Map和Nacos注册服务器端接口
1.对于服务器端接口函数，我开始利用全局Map进行存储，通过访问Map就可以获取对应的接口，存在缺点，分布式系统中，可能需要在每一个服务器端都需要一个Map，浪费空间
2.利用Nacos提供的服务，可以在访问Nacos获取对应服务器中的接口，同时在服务器开启时候进行初始化处理，避免之前注册服务造成影响

##
### 4. 负载均衡算法
 采用了随机算法，轮询算法，一致性哈希算法，可能实现服务器端的均衡调用，提高每台服务器的性能
 
##
### 5.JDK动态代理
1. 利用了JDK动态代理实现了客户端的自动实现接口调用，简化了客户端操作
2.动态代理主要是接口InvocaHandle和newProxyInstance()
@Data
public
class ProxyNettyClient implements InvocationHandler {
    private RpcClient rpcClient;

    public ProxyNettyClient(RpcClient rpcClient){
        this.rpcClient=rpcClient;
    }
    public  <T>T getProxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
       RpcRequest rpcRequest=RpcRequest.builder()
               .InterfaceName(method.getDeclaringClass().getName())
               .methodName(method.getName())
               .clazz(method.getParameterTypes())
               .params(args)
               .build();
        return rpcClient.sendRequest(rpcRequest);
    }
}

-----

本项目主要是学习了Netty,NIO,BIO,负载均衡算法等知识后，实现一个简易的RPC框架。
