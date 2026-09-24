package com.example.campus_placement_management_system.company.dto;

import jakarta.validation.constraints.*;

public record CompanyRequestDto (
        @NotBlank(message = "Company name is required")
        @Size(max = 100, message = "Company name cannot exceed 100 characters")
        String name,

        @NotBlank(message = "Company email is required")
        @Email(message = "Enter a valid company email")
        String email,

        String website,

        @NotBlank(message = "Industry is required")
        String industry,

        @NotBlank(message = "Location is required")
        String location,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @NotBlank(message = "Contact person is required")
        String contactPerson,

        @NotBlank(message = "Contact email is required")
        @Email(message = "Enter a valid contact email")
        String contactEmail,

        @NotBlank(message = "Contact phone is required")
        @Pattern(
                regexp = "^[0-9]{10}$",
                message = "Contact phone must contain 10 digits"
        )
        String contactPhone

) {}