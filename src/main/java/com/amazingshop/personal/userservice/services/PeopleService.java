package com.amazingshop.personal.userservice.services;

import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.repositories.PeopleRepository;
import com.amazingshop.personal.userservice.util.exceptions.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Optional<Person> findPersonByPersonName(String username) {
        log.debug("Searching for person by username: {}", username);
        return peopleRepository.findPersonByUsername(username);
    }

    public Optional<Person> findPersonByEmail(String email) {
        log.debug("Searching for person by email: {}", email);
        return peopleRepository.findPersonByEmail(email);
    }

    public Person findPersonByIdOrThrow(Long id) {
        return peopleRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person with id " + id + " not found"));
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public boolean existsByUsername(String username) {
        return peopleRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return peopleRepository.existsByEmail(email);
    }

    @Transactional
    public Person save(Person person) {
        log.debug("Saving person: {}", person.getUsername());
        return peopleRepository.save(person);
    }

    @Transactional
    public void deleteById(Long id) {
        log.info("Deleting person with id: {}", id);
        if (!peopleRepository.existsById(id)) {
            throw new PersonNotFoundException("Person with id " + id + " not found");
        }
        peopleRepository.deleteById(id);
    }
}