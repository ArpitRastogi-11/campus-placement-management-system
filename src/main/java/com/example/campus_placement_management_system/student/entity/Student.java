package com.example.campus_placement_management_system.student.entity;

import com.example.campus_placement_management_system.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String enrollmentNumber;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String course;

    @Column(nullable = false)
    private Integer graduationYear;

    @Column(nullable = false)
    private Double cgpa;

    private String phone;

    private String resumeUrl;
}