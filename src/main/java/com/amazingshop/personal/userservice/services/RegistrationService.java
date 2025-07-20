package com.amazingshop.personal.userservice.services;

import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.enums.Role;
import com.amazingshop.personal.userservice.util.validators.PersonValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@Slf4j
public class RegistrationService {

    private final PeopleService peopleService;
    private final PasswordEncoder passwordEncoder;
    private final PersonValidator personValidator;

    public RegistrationService(PeopleService peopleService,
                               PasswordEncoder passwordEncoder,
                               PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.passwordEncoder = passwordEncoder;
        this.personValidator = personValidator;
    }

    @Transactional
    public Person register(Person person) {
        log.info("Attempting to register user: {}", person.getUsername());

        // Валидация перед сохранением
        personValidator.validateAndThrow(person);

        // Подготовка данных
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setCreatedAt(LocalDateTime.now());
        person.setRole(Role.USER);

        // Сохранение
        Person savedPerson = peopleService.save(person);

        log.info("User successfully registered: {}", savedPerson.getUsername());
        return savedPerson;
    }
}