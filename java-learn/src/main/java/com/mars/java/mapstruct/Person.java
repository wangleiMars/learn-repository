package com.mars.java.mapstruct;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Person {
    String describe;

    private String id;

    private String name;

    private int age;

    private BigDecimal source;

    private double height;

    private Date createTime;
}
