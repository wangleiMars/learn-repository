package com.mars.java.mapstruct;

import java.text.SimpleDateFormat;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-18T15:07:40+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (Azul Systems, Inc.)"
)
public class PersonMapperImpl implements PersonMapper {

    @Override
    public PersonDTO conver(Person person) {
        if ( person == null ) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();

        personDTO.setPersonName( person.getName() );
        if ( person.getCreateTime() != null ) {
            personDTO.setCreateTime( new SimpleDateFormat( "yyyyMMddHHmmss" ).format( person.getCreateTime() ) );
        }
        personDTO.setDescribe( person.getDescribe() );
        personDTO.setAge( String.valueOf( person.getAge() ) );
        if ( person.getSource() != null ) {
            personDTO.setSource( person.getSource().toString() );
        }
        personDTO.setHeight( String.valueOf( person.getHeight() ) );

        return personDTO;
    }
}
