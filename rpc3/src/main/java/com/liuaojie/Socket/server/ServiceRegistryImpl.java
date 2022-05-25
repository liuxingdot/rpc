package com.liuaojie.Socket.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistryImpl<T> implements ServiceRegistry<T> {
    Logger logger= LoggerFactory.getLogger(ServiceRegistryImpl.class);
    private final static Map<String,Object> serverMap=new ConcurrentHashMap<>();
    private final static Set<String> selectedServer=ConcurrentHashMap.newKeySet();

    @Override
    public void register(T service) {
        String serverName = service.getClass().getCanonicalName();
        if(selectedServer.contains(serverName)){
            return;
        }
        Class<?>[] interfaces = service.getClass().getInterfaces();
        selectedServer.add(serverName);
        for(Class i:interfaces){
            serverMap.put(i.getCanonicalName(),service);
        }
        System.out.println(service+"区别"+serverName);
        logger.info("向{}接口中注册{}服务！",interfaces,serverName);
    }

    @Override
    public Object getService(String ServiceName) {
        return serverMap.get(ServiceName);
    }
}
