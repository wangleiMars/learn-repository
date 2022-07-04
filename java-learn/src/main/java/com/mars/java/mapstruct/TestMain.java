package com.mars.java.mapstruct;
import java.math.BigDecimal;
import java.util.Date;

public class TestMain {
    public static void main(String[] args) {
        Person person = new Person();
        person.setDescribe("1");
        person.setId("2");
        person.setName("3");
        person.setAge(3);
        person.setSource(new BigDecimal("5"));
        person.setHeight(6.0D);
        person.setCreateTime(new Date());
        PersonDTO personDTO =   PersonDTO.PersonMapperIn.INSTANCT.conver(person);
        System.out.println(personDTO);
        personDTO.setId(111l);
        Person person1 = PersonDTO.PersonMapperIn.INSTANCT.conver(personDTO);
        System.out.println(person1);
    }
}
