package com.amazingshop.personal.userservice.controllers;

import com.amazingshop.personal.userservice.dto.requests.PersonDTO;
import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.security.details.PersonDetailsImpl;
import com.amazingshop.personal.userservice.services.AdminService;
import com.amazingshop.personal.userservice.services.ConverterService;
import com.amazingshop.personal.userservice.services.PeopleService;
import com.amazingshop.personal.userservice.dto.responses.PersonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UsersController {

    private final AdminService adminService;
    private final PeopleService peopleService;
    private final ConverterService converterService;

    @Autowired
    public UsersController(AdminService adminService, PeopleService peopleService, ConverterService converterService) {
        this.adminService = adminService;
        this.peopleService = peopleService;
        this.converterService = converterService;
    }

    /**
     * Получение информации о текущем пользователе
     * GET /api/v1/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<PersonDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetailsImpl personDetails = (PersonDetailsImpl) authentication.getPrincipal();

        Person person = peopleService.findPersonByPersonName(personDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        PersonDTO personDTO = converterService.convertedToPersonDTO(person);
        log.info("User info requested: {}", person.getUsername());

        return ResponseEntity.ok(personDTO);
    }

    /**
     * Обновление информации о текущем пользователе
     * PUT /api/v1/users/me
     */
    @PutMapping("/me")
    public ResponseEntity<PersonDTO> updateCurrentUser(@RequestBody PersonDTO personDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetailsImpl personDetails = (PersonDetailsImpl) authentication.getPrincipal();

        Person currentPerson = peopleService.findPersonByPersonName(personDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        // Обновляем только разрешенные поля
        currentPerson.setEmail(personDTO.getEmail());
        // Не обновляем username, role, password через этот эндпоинт

        Person updatedPerson = peopleService.save(currentPerson);
        PersonDTO updatedDTO = converterService.convertedToPersonDTO(updatedPerson);

        log.info("User info updated: {}", updatedPerson.getUsername());
        return ResponseEntity.ok(updatedDTO);
    }

    /**
     * Админский эндпоинт - приветствие
     * GET /api/v1/users/admin/hello
     */
    @GetMapping("/admin/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> helloForAdmin() {
        String message = adminService.sayForAdmin();
        log.info("Admin hello endpoint accessed");
        return ResponseEntity.ok(Map.of("message", message));
    }

    /**
     * Админский эндпоинт - получение всех пользователей
     * GET /api/v1/users/admin/all
     */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonResponse> getAllUsers() {
        List<Person> persons = peopleService.findAll();
        List<PersonDTO> personDTOs = persons.stream()
                .map(converterService::convertedToPersonDTO)
                .toList();

        log.info("All users requested by admin, count: {}", personDTOs.size());
        return ResponseEntity.ok(new PersonResponse(personDTOs));
    }

    /**
     * Админский эндпоинт - получение пользователя по ID
     * GET /api/v1/users/admin/{id}
     */
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonDTO> getUserById(@PathVariable Long id) {
        Person person = peopleService.findPersonByIdOrThrow(id);
        PersonDTO personDTO = converterService.convertedToPersonDTO(person);

        log.info("User requested by admin: {}", person.getUsername());
        return ResponseEntity.ok(personDTO);
    }

    /**
     * Проверка работоспособности сервиса
     * GET /api/v1/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Users service is running");
    }
}
