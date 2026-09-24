package com.example.campus_placement_management_system.company.controller;

import com.example.campus_placement_management_system.company.dto.CompanyRequestDto;
import com.example.campus_placement_management_system.company.dto.CompanyResponseDto;
import com.example.campus_placement_management_system.company.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(
            @Valid @RequestBody CompanyRequestDto request) {

        CompanyResponseDto response =
                companyService.createCompany(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<CompanyResponseDto>> getCompanies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {

        return ResponseEntity.ok(
                companyService.searchCompanies(
                        name,
                        industry,
                        location,
                        active,
                        pageable
                )
        );
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(
            @PathVariable Long companyId) {

        return ResponseEntity.ok(
                companyService.getCompanyById(companyId)
        );
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> updateCompany(
            @PathVariable Long companyId,
            @Valid @RequestBody CompanyRequestDto request) {

        return ResponseEntity.ok(
                companyService.updateCompany(
                        companyId,
                        request
                )
        );
    }

    @PatchMapping("/{companyId}/status")
    public ResponseEntity<CompanyResponseDto> updateCompanyStatus(
            @PathVariable Long companyId,
            @RequestParam boolean active) {

        return ResponseEntity.ok(
                companyService.updateCompanyStatus(
                        companyId,
                        active
                )
        );
    }


}
