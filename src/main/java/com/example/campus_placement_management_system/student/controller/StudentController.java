package com.example.campus_placement_management_system.student.controller;

import com.example.campus_placement_management_system.auth.security.CustomUserDetails;
import com.example.campus_placement_management_system.student.dto.StudentRequest;
import com.example.campus_placement_management_system.student.dto.StudentResponse;
import com.example.campus_placement_management_system.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // Create student profile
    @PostMapping("/profile")
    public ResponseEntity<StudentResponse> createProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody StudentRequest request) {

        Long userId = userDetails.getUser().getId();

        StudentResponse response =
                studentService.createStudent(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get logged-in student's profile
    @GetMapping("/profile")
    public ResponseEntity<StudentResponse> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();

        return ResponseEntity.ok(
                studentService.getStudentByUserId(userId)
        );
    }

    // Update logged-in student's profile
    @PutMapping("/profile")
    public ResponseEntity<StudentResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody StudentRequest request) {

        Long userId = userDetails.getUser().getId();

        StudentResponse response =
                studentService.updateStudent(userId, request);

        return ResponseEntity.ok(response);
    }
}