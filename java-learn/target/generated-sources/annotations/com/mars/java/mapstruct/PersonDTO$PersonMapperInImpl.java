package com.mars.java.mapstruct;

import com.mars.java.mapstruct.PersonDTO.PersonMapperIn;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-18T15:17:28+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (Azul Systems, Inc.)"
)
public class PersonDTO$PersonMapperInImpl implements PersonMapperIn {

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

    @Override
    public Person conver(PersonDTO person) {
        if ( person == null ) {
            return null;
        }

        Person person1 = new Person();

        person1.setName( person.getPersonName() );
        try {
            if ( person.getCreateTime() != null ) {
                person1.setCreateTime( new SimpleDateFormat( "yyyyMMddHHmmss" ).parse( person.getCreateTime() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        person1.setDescribe( person.getDescribe() );
        if ( person.getId() != null ) {
            person1.setId( String.valueOf( person.getId() ) );
        }
        if ( person.getAge() != null ) {
            person1.setAge( Integer.parseInt( person.getAge() ) );
        }
        if ( person.getSource() != null ) {
            person1.setSource( new BigDecimal( person.getSource() ) );
        }
        if ( person.getHeight() != null ) {
            person1.setHeight( Double.parseDouble( person.getHeight() ) );
        }

        return person1;
    }
}
