package com.example.campus_placement_management_system.student.dto;

public record StudentResponse(

        Long id,
        Long userId,
        String name,
        String email,
        String enrollmentNumber,
        String department,
        String course,
        Integer graduationYear,
        Double cgpa,
        String phone,
        String resumeUrl
) {
}
