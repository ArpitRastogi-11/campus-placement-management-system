package com.example.campus_placement_management_system.company.repository;

import com.example.campus_placement_management_system.company.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository
        extends JpaRepository<Company, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    Optional<Company> findByEmailIgnoreCase(String email);

    Page<Company> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Company> findByIndustryIgnoreCase(
            String industry,
            Pageable pageable
    );

    Page<Company> findByLocationIgnoreCase(
            String location,
            Pageable pageable
    );

    Page<Company> findByActive(
            boolean active,
            Pageable pageable
    );
}
