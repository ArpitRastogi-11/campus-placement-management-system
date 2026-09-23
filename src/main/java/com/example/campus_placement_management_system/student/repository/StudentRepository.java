package com.example.campus_placement_management_system.student.repository;

import com.example.campus_placement_management_system.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByEnrollmentNumber(String enrollmentNumber);

    Page<Student> findByDepartmentIgnoreCase(
            String department,
            Pageable pageable
    );

    Page<Student> findByCourseIgnoreCase(
            String course,
            Pageable pageable
    );

    Page<Student> findByDepartmentIgnoreCaseAndCourseIgnoreCase(
            String department,
            String course,
            Pageable pageable
    );
}