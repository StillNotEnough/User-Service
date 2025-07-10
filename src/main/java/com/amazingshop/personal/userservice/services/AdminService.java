package com.amazingshop.personal.userservice.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService{

    @PreAuthorize("hasRole('ADMIN')")
    public String sayForAdmin(){
        return "Only admins";
    }
}
