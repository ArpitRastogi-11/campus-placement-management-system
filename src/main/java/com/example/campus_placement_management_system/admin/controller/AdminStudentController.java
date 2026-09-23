package com.example.campus_placement_management_system.admin.controller;

import com.example.campus_placement_management_system.admin.service.AdminStudentService;
import com.example.campus_placement_management_system.student.dto.StudentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final AdminStudentService adminStudentService;

    @GetMapping
    public ResponseEntity<Page<StudentResponse>> getAllStudents(

            @RequestParam(required = false)
            String department,

            @RequestParam(required = false)
            String course,

            Pageable pageable) {

        return ResponseEntity.ok(
                adminStudentService.getAllStudents(
                        department,
                        course,
                        pageable
                )
        );
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                adminStudentService.getStudentById(studentId)
        );
    }

    @PatchMapping("/{studentId}/status")
    public ResponseEntity<StudentResponse> updateStudentStatus(
            @PathVariable Long studentId,
            @RequestParam boolean enabled) {

        return ResponseEntity.ok(
                adminStudentService.updateStudentStatus(
                        studentId,
                        enabled
                )
        );
    }
}
