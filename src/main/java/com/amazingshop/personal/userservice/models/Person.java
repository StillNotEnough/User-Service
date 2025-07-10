package com.amazingshop.personal.userservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Person")
@Data
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @NotEmpty(message = "Username should be not empty")
    @Size(min = 2, max = 30, message = "Username should be for 2 to 30 symbols")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Password should be not empty")
    private String password;

    @Column(name = "email")
    @NotEmpty(message = "Email should be not empty")
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "role")
    @Enumerated
    private Role role;

    public Person(){

    }

    public Person(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
