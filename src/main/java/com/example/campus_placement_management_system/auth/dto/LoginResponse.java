package com.example.campus_placement_management_system.auth.dto;

import com.example.campus_placement_management_system.auth.entity.Role;

public record LoginResponse(

        String token,
        String tokenType,
        Long userId,
        String name,
        String email,
        Role role
) {
}
