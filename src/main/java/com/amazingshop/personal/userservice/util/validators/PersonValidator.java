package com.amazingshop.personal.userservice.util.validators;

import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.services.PeopleService;
import com.amazingshop.personal.userservice.util.exceptions.PersonValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (person.getUsername() == null || person.getUsername().isEmpty()){
            throw new PersonValidationException("Username cannot be empty!");
        }

        Optional<Person> existingPerson = peopleService.findPersonByPersonName(person.getUsername());

        if (existingPerson.isPresent()) {
            throw new PersonValidationException("A person with this username already exists!");
        }
    }

    public void validateAndThrow(Person person) {
        if (person.getUsername() == null || person.getUsername().isEmpty()) {
            throw new PersonValidationException("Username cannot be empty!");
        }

        Optional<Person> existingPerson = peopleService.findPersonByPersonName(person.getUsername());

        if (existingPerson.isPresent()) {
            throw new PersonValidationException("A person with this username already exists!");
        }
    }
}
