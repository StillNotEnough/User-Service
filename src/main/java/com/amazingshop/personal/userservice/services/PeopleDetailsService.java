package com.amazingshop.personal.userservice.services;

import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.security.details.PersonDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PeopleDetailsService implements UserDetailsService {

    private final PeopleService peopleService;

    @Autowired
    public PeopleDetailsService(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        Optional<Person> person = peopleService.findPersonByPersonName(username);

        if (person.isEmpty()) {
            log.warn("User not found: {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        log.debug("User found: {}", username);
        return new PersonDetailsImpl(person.get());
    }
}