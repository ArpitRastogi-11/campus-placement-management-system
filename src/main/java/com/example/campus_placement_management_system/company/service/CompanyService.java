package com.example.campus_placement_management_system.company.service;


import com.example.campus_placement_management_system.company.dto.CompanyRequestDto;
import com.example.campus_placement_management_system.company.dto.CompanyResponseDto;
import com.example.campus_placement_management_system.company.entity.Company;
import com.example.campus_placement_management_system.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto request) {

        if (companyRepository.existsByNameIgnoreCase(request.name())) {
            throw new RuntimeException("Company name already exists");
        }

        if (companyRepository.existsByEmailIgnoreCase(request.email())) {
            throw new RuntimeException("Company email already exists");
        }

        Company company = Company.builder()
                .name(request.name())
                .email(request.email())
                .website(request.website())
                .industry(request.industry())
                .location(request.location())
                .description(request.description())
                .contactPerson(request.contactPerson())
                .contactEmail(request.contactEmail())
                .contactPhone(request.contactPhone())
                .active(true)
                .build();

        return mapToResponse(companyRepository.save(company));
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto getCompanyById(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company not found"));

        return mapToResponse(company);
    }

    @Transactional(readOnly = true)
    public Page<CompanyResponseDto> getAllCompanies(Pageable pageable) {

        return companyRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    private CompanyResponseDto mapToResponse(Company company) {

        return new CompanyResponseDto(
                company.getId(),
                company.getName(),
                company.getEmail(),
                company.getWebsite(),
                company.getIndustry(),
                company.getLocation(),
                company.getDescription(),
                company.getContactPerson(),
                company.getContactEmail(),
                company.getContactPhone(),
                company.isActive(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }

    @Transactional
    public CompanyResponseDto updateCompany(
            Long companyId,
            CompanyRequestDto request) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company not found"));

        if (companyRepository.existsByNameIgnoreCaseAndIdNot(
                request.name(), companyId)) {

            throw new RuntimeException("Company name already exists");
        }

        if (companyRepository.existsByEmailIgnoreCaseAndIdNot(
                request.email(), companyId)) {

            throw new RuntimeException("Company email already exists");
        }

        company.setName(request.name());
        company.setEmail(request.email());
        company.setWebsite(request.website());
        company.setIndustry(request.industry());
        company.setLocation(request.location());
        company.setDescription(request.description());
        company.setContactPerson(request.contactPerson());
        company.setContactEmail(request.contactEmail());
        company.setContactPhone(request.contactPhone());

        return mapToResponse(companyRepository.save(company));
    }

    @Transactional
    public CompanyResponseDto updateCompanyStatus(
            Long companyId,
            boolean active) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException("Company not found"));

        company.setActive(active);

        return mapToResponse(companyRepository.save(company));
    }
    @Transactional(readOnly = true)
    public Page<CompanyResponseDto> searchCompanies(
            String name,
            String industry,
            String location,
            Boolean active,
            Pageable pageable) {

        Page<Company> companies;

        if (name != null) {

            companies = companyRepository
                    .findByNameContainingIgnoreCase(name, pageable);

        } else if (industry != null) {

            companies = companyRepository
                    .findByIndustryIgnoreCase(industry, pageable);

        } else if (location != null) {

            companies = companyRepository
                    .findByLocationIgnoreCase(location, pageable);

        } else if (active != null) {

            companies = companyRepository
                    .findByActive(active, pageable);

        } else {

            companies = companyRepository.findAll(pageable);
        }

        return companies.map(this::mapToResponse);
    }
}