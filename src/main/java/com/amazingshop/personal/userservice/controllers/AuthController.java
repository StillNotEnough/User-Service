package com.amazingshop.personal.userservice.controllers;


import com.amazingshop.personal.userservice.dto.AuthenticationDTO;
import com.amazingshop.personal.userservice.dto.PersonDTO;
import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.security.JwtUtil;
import com.amazingshop.personal.userservice.services.ConverterService;
import com.amazingshop.personal.userservice.services.RegistrationService;
import com.amazingshop.personal.userservice.util.JwtResponse;
import com.amazingshop.personal.userservice.util.validators.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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

    // регистрация
    @PostMapping("/signup")
    public JwtResponse performRegistration(@RequestBody @Valid PersonDTO personDTO) {
        Person person = converterService.convertedToPerson(personDTO);

        personValidator.validateAndThrow(person);

        registrationService.register(person);

        // возвращаем Jwt token
        String token = jwtUtil.generateToken(person.getUsername());
        long expiresIn = jwtUtil.getExpiration();
        return new JwtResponse(token, expiresIn);
    }

    // вход в уже существующий акк и генерация нового токена
    @PostMapping("/login")
    public JwtResponse performLogin(@RequestBody AuthenticationDTO authenticationDTO){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationDTO.getUsername(),
                authenticationDTO.getPassword()));

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        long expiresIn = jwtUtil.getExpiration();
        return new JwtResponse(token, expiresIn);
    }
}