package com.liuaojie.Socket.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data

public class HelloObject implements Serializable {
    private int age;
    private String message;


    public HelloObject(int age,String message){
        this.age=age;
        this.message=message;
    }
    public HelloObject(){};
}
