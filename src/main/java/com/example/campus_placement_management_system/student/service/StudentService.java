package com.example.campus_placement_management_system.student.service;

import com.example.campus_placement_management_system.auth.entity.User;
import com.example.campus_placement_management_system.auth.repository.UserRepository;
import com.example.campus_placement_management_system.student.dto.StudentRequest;
import com.example.campus_placement_management_system.student.dto.StudentResponse;
import com.example.campus_placement_management_system.student.entity.Student;
import com.example.campus_placement_management_system.student.repository.StudentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Transactional
    public StudentResponse createStudent(
            Long userId,
            StudentRequest request) {

        // 1. Check whether user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // 2. Check whether user already has a student profile
        if (studentRepository.existsByUserId(userId)) {
            throw new RuntimeException(
                    "Student profile already exists for this user");
        }

        // 3. Check duplicate enrollment number
        if (studentRepository.existsByEnrollmentNumber(
                request.enrollmentNumber())) {

            throw new RuntimeException(
                    "Enrollment number already exists");
        }

        // 4. Create Student entity
        Student student = Student.builder()
                .user(user)
                .enrollmentNumber(request.enrollmentNumber())
                .department(request.department())
                .course(request.course())
                .graduationYear(request.graduationYear())
                .cgpa(request.cgpa())
                .phone(request.phone())
                .resumeUrl(request.resumeUrl())
                .build();

        // 5. Save to database
        Student savedStudent = studentRepository.save(student);

        // 6. Convert Entity → Response DTO
        return mapToResponse(savedStudent);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentByUserId(Long userId) {

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Student profile not found"));

        return mapToResponse(student);
    }

    private StudentResponse mapToResponse(Student student) {

        User user = student.getUser();

        return new StudentResponse(
                student.getId(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                student.getEnrollmentNumber(),
                student.getDepartment(),
                student.getCourse(),
                student.getGraduationYear(),
                student.getCgpa(),
                student.getPhone(),
                student.getResumeUrl()
        );
    }

    @Transactional
    public StudentResponse updateStudent(
            Long userId,
            StudentRequest request) {

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Student profile not found"));

        if (!student.getEnrollmentNumber()
                .equals(request.enrollmentNumber())
                && studentRepository.existsByEnrollmentNumber(
                request.enrollmentNumber())) {

            throw new RuntimeException(
                    "Enrollment number already exists");
        }

        student.setEnrollmentNumber(request.enrollmentNumber());
        student.setDepartment(request.department());
        student.setCourse(request.course());
        student.setGraduationYear(request.graduationYear());
        student.setCgpa(request.cgpa());
        student.setPhone(request.phone());
        student.setResumeUrl(request.resumeUrl());

        Student updatedStudent = studentRepository.save(student);

        return mapToResponse(updatedStudent);
    }
}