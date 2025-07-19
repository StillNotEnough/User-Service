package com.amazingshop.personal.userservice.util.validators;

import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.services.PeopleService;
import com.amazingshop.personal.userservice.util.exceptions.PersonValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

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

        try {
            validateAndThrow(person);
        }catch (PersonValidationException e){
            errors.rejectValue("username", "", e.getMessage());
        }
    }

    public void validateAndThrow(Person person) {
        validateUsername(person.getUsername());
        validateEmail(person.getEmail());
        validatePassword(person.getPassword());
    }

    public void validateUsername(String username){
        if (username == null || username.trim().isEmpty()) {
            throw new PersonValidationException("Username cannot be empty!");
        }

        if (username.length() < 2 || username.length() > 30){
            throw new PersonValidationException("Username should be between 2 and 30 characters!");
        }

        Optional<Person> existingPerson = peopleService.findPersonByPersonName(username);
        if (existingPerson.isPresent()){
            throw new PersonValidationException("A person with this username already exists!");
        }
    }

    public void validateEmail(String email){
        if (email == null || email.trim().isEmpty()) {
            throw new PersonValidationException("Email cannot be empty!");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new PersonValidationException("Invalid email format!");
        }

        Optional<Person> existingPerson = peopleService.findPersonByEmail(email);
        if (existingPerson.isPresent()) {
            throw new PersonValidationException("A person with this email already exists!");
        }
    }

    public void validatePassword(String password){
        if (password == null || password.trim().isEmpty()) {
            throw new PersonValidationException("Password cannot be empty!");
        }

        if (password.length() < 6) {
            throw new PersonValidationException("Password should be at least 6 characters long!");
        }
    }
}