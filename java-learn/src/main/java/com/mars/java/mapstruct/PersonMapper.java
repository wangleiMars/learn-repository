package com.mars.java.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCT = Mappers.getMapper(PersonMapper.class);

    @Mapping(source = "name", target = "personName")
    @Mapping(target = "id", ignore = true) // 忽略id，不进行映射
    @Mapping(target = "createTime", source = "createTime",dateFormat ="yyyyMMddHHmmss" )
    PersonDTO conver(Person person);
}
