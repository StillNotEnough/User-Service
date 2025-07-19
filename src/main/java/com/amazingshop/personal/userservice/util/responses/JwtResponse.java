package com.amazingshop.personal.userservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private long expiresIn;
    private String type = "Bearer";
    private String username;

    public JwtResponse(String token, long expiresIn, String username) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.username = username;
    }
}