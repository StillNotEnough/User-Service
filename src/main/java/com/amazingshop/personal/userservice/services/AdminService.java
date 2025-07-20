package com.amazingshop.personal.userservice.services;

import com.amazingshop.personal.userservice.enums.Role;
import com.amazingshop.personal.userservice.models.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminService {

    private final PeopleService peopleService;

    public AdminService(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String sayForAdmin() {
        return "Only admins can see this message";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Person> getAllUsers() {
        log.info("Admin requested all users list");
        return peopleService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long userId) {
        log.info("Admin requested to delete user with id: {}", userId);
        peopleService.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Person promoteToAdmin(Long userId) {
        log.info("Admin requested to promote user {} to admin", userId);
        Person person = peopleService.findPersonByIdOrThrow(userId);
        person.setRole(Role.ADMIN);
        return peopleService.save(person);
    }
}