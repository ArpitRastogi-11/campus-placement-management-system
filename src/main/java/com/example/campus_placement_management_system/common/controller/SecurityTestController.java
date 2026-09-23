package com.example.campus_placement_management_system.common.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class SecurityTestController {

    @GetMapping("/me")
    public String getCurrentUser(Authentication authentication) {

        return "Username: " + authentication.getName()
                + " | Authorities: " + authentication.getAuthorities();
    }
}