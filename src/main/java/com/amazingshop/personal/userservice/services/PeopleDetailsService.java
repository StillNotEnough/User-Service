package com.amazingshop.personal.userservice.services;

import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.security.details.PersonDetailsImpl;
import com.amazingshop.personal.userservice.util.exceptions.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PeopleDetailsService implements UserDetailsService {

    private final PeopleService peopleService;

    @Autowired
    public PeopleDetailsService(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleService.findPersonByPersonName(username);

        if (person.isEmpty()){
            throw new PersonNotFoundException("Person with this username is not found!");
        }

        return new PersonDetailsImpl(person.get());
    }
}
