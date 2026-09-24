package com.example.campus_placement_management_system.company.dto;

import java.time.LocalDateTime;

public record CompanyResponseDto(
        Long id,
        String name,
        String email,
        String website,
        String industry,
        String location,
        String description,
        String contactPerson,
        String contactEmail,
        String contactPhone,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
