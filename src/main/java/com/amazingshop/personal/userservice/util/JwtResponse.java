package com.amazingshop.personal.userservice.util;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private long expiresIn;

    public JwtResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}