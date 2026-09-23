package com.example.campus_placement_management_system.admin.service;

import com.example.campus_placement_management_system.auth.entity.User;
import com.example.campus_placement_management_system.student.dto.StudentResponse;
import com.example.campus_placement_management_system.student.entity.Student;
import com.example.campus_placement_management_system.student.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public Page<StudentResponse> getAllStudents(
            String department,
            String course,
            Pageable pageable) {

        Page<Student> students;

        if (department != null && course != null) {

            students =
                    studentRepository
                            .findByDepartmentIgnoreCaseAndCourseIgnoreCase(
                                    department,
                                    course,
                                    pageable
                            );

        } else if (department != null) {

            students =
                    studentRepository
                            .findByDepartmentIgnoreCase(
                                    department,
                                    pageable
                            );

        } else if (course != null) {

            students =
                    studentRepository
                            .findByCourseIgnoreCase(
                                    course,
                                    pageable
                            );

        } else {

            students = studentRepository.findAll(pageable);
        }

        return students.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        return mapToResponse(student);
    }
    @Transactional
    public StudentResponse updateStudentStatus(
            Long studentId,
            boolean enabled) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        User user = student.getUser();

        user.setEnabled(enabled);

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
}
