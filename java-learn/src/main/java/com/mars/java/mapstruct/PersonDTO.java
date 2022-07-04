package com.mars.java.mapstruct;

import lombok.Data;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Data
public class PersonDTO {
    String describe;

    private Long id;

    private String personName;

    private String age;

    private String source;

    private String height;

    private String createTime;

    @Mapper
    public interface PersonMapperIn {
        PersonMapperIn INSTANCT = Mappers.getMapper(PersonDTO.PersonMapperIn.class);

        @Mapping(source = "name", target = "personName")
        @Mapping(target = "id", ignore = true) // 忽略id，不进行映射
        @Mapping(target = "createTime", source = "createTime",dateFormat ="yyyyMMddHHmmss" )
        PersonDTO conver(Person person);

        @InheritInverseConfiguration
        Person  conver(PersonDTO person);
    }
}
