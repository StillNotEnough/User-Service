package com.amazingshop.personal.userservice.controllers;


import com.amazingshop.personal.userservice.dto.AuthenticationDTO;
import com.amazingshop.personal.userservice.dto.PersonDTO;
import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.security.JwtUtil;
import com.amazingshop.personal.userservice.services.ConverterService;
import com.amazingshop.personal.userservice.services.RegistrationService;
import com.amazingshop.personal.userservice.util.responses.JwtResponse;
import com.amazingshop.personal.userservice.util.validators.PersonValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final JwtUtil jwtUtil;
    private final ConverterService converterService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService,
                          JwtUtil jwtUtil, ConverterService converterService, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.converterService = converterService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Регистрация нового пользователя
     * POST /api/v1/auth/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> performRegistration(@RequestBody @Valid PersonDTO personDTO) {
        log.info("Registration attempt for username: {}", personDTO.getUsername());

        Person person = converterService.convertedToPerson(personDTO);
        personValidator.validateAndThrow(person);
        registrationService.register(person);

        String token = jwtUtil.generateToken(person.getUsername());
        long expiresIn = jwtUtil.getExpirationTime();

        log.info("User registered successfully: {}", person.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new JwtResponse(token, expiresIn, person.getUsername()));
    }

    /**
     * Вход в существующий аккаунт
     * POST /api/v1/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> performLogin(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        log.info("Login attempt for username: {}", authenticationDTO.getUsername());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationDTO.getUsername(),
                authenticationDTO.getPassword()));

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        long expiresIn = jwtUtil.getExpirationTime();

        log.info("User logged in successfully: {}", authenticationDTO.getUsername());
        return ResponseEntity.ok(new JwtResponse(token, expiresIn, authenticationDTO.getUsername()));
    }

    /**
     * Проверка работоспособности сервиса
     * GET /api/v1/auth/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Auth service is running");
    }
}