package com.liuaojie.Socket.server;

import com.liuaojie.Socket.client.HelloObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServerImpl implements HelloServer {
    private Logger logger=LoggerFactory.getLogger(HelloServerImpl.class);


    @Override
    public String hello(HelloObject helloObject) {
       logger.info("这是一条年龄信息："+helloObject.getMessage());
       return "具体年龄:"+helloObject.getAge();
    }
}
