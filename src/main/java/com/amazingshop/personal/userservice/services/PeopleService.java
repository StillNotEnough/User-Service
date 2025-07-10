package com.amazingshop.personal.userservice.services;

import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Optional<Person> findPersonByPersonName(String username){
        return peopleRepository.findPersonByUsername(username);
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }
}
