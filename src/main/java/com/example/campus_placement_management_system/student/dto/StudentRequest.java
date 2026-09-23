package com.example.campus_placement_management_system.student.dto;

import jakarta.validation.constraints.*;

    public record StudentRequest(

            @NotBlank(message = "Enrollment number is required")
            String enrollmentNumber,

            @NotBlank(message = "Department is required")
            String department,

            @NotBlank(message = "Course is required")
            String course,

            @NotNull(message = "Graduation year is required")
            @Min(value = 2020, message = "Invalid graduation year")
            Integer graduationYear,

            @NotNull(message = "CGPA is required")
            @DecimalMin(value = "0.0", message = "CGPA cannot be less than 0")
            @DecimalMax(value = "10.0", message = "CGPA cannot be greater than 10")
            Double cgpa,

            @Pattern(
                    regexp = "^[0-9]{10}$",
                    message = "Phone number must contain 10 digits"
            )
            String phone,

            String resumeUrl
    ) {
    }
