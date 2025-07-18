package com.amazingshop.personal.userservice.controllers;

import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.security.details.PersonDetailsImpl;
import com.amazingshop.personal.userservice.services.AdminService;
import com.amazingshop.personal.userservice.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final AdminService adminService;
    private final PeopleService peopleService;

    @Autowired
    public AdminController(AdminService adminService, PeopleService peopleService) {
        this.adminService = adminService;
        this.peopleService = peopleService;
    }

    @GetMapping("/me")
    public String showMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetailsImpl personDetails = (PersonDetailsImpl) authentication.getPrincipal();
        Optional<Person> person = peopleService.findPersonByPersonName(personDetails.getUsername());

        return person.get().getUsername() + "\n" + person.get().getId() + "\n" +
                person.get().getEmail() + "\n" + person.get().getRole() + "\n" + person.get().getPassword();
    }

    @GetMapping("/helloAdmin")
    public String helloForAdmin() {
        return adminService.sayForAdmin();
    }
}
