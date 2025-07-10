package com.amazingshop.personal.userservice.services;

import com.amazingshop.personal.userservice.dto.PersonDTO;
import com.amazingshop.personal.userservice.models.Person;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConverterService {

    private final ModelMapper modelMapper;

    @Autowired
    public ConverterService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Person convertedToPerson(PersonDTO personDTO){
        return modelMapper.map(personDTO, Person.class);
    }

    public PersonDTO convertedToPersonDto(Person person){
        return modelMapper.map(person, PersonDTO.class);
    }
}
