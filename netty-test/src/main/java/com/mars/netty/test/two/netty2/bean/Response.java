package com.mars.netty.test.two.netty2.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Response {
    private long id;
    private Object result;
}
