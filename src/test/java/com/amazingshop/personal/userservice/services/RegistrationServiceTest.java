package com.amazingshop.personal.userservice.services;

import com.amazingshop.personal.userservice.enums.Role;
import com.amazingshop.personal.userservice.models.Person;
import com.amazingshop.personal.userservice.util.validators.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private PeopleService peopleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PersonValidator personValidator;

    @InjectMocks
    private RegistrationService registrationService;

    private Person testPerson;

    @BeforeEach
    void setUp(){
        testPerson = new Person("testUser", "testPassword", "test@example.com");
    }

    @Test
    void register_shouldEncodePasswordAndSetUserRole(){
        // Arrange (подготовка)
        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");
        when(peopleService.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0, Person.class));

        // Act (действие)
        Person registratedPerson = registrationService.register(testPerson);

        // Assert (проверка)
        assertEquals("encodedPassword", registratedPerson.getPassword(), "Password should be encoded");
        assertEquals(Role.USER, registratedPerson.getRole(), "Role should be set to USER by default");
        assertNotNull(registratedPerson.getCreatedAt(), "Creation date should be set");

        // Verify (проверка вызовов)
        verify(personValidator, times(1)).validateAndThrow(testPerson);
        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(peopleService, times(1)).save(any(Person.class));
    }
}
