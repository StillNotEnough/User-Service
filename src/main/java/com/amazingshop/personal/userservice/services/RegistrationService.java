package com.amazingshop.personal.userservice.services;

import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.models.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class RegistrationService {

    private final PeopleService peopleService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(PeopleService peopleService, PasswordEncoder passwordEncoder) {
        this.peopleService = peopleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setCreatedAt(LocalDateTime.now());
        person.setRole(Role.USER);
        peopleService.save(person);
    }
}
