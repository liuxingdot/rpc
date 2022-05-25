package com.liuaojie.Socket.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest<T> implements Serializable {
    /*
    请求对象名称
     */
    private String InterfaceName;

    /*
        请求对象方法
         */
    private String methodName;
    /*
    请求参数类型
     */
    private Class<?>[] clazz;
    /*
    请求参数
     */
    private Object[] params;

}
