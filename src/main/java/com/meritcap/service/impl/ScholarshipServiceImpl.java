package com.meritcap.service.impl;

import com.meritcap.DTOs.requestDTOs.scholarship.ScholarshipRequestDto;
import com.meritcap.DTOs.responseDTOs.scholarship.ScholarshipResponseDto;
import com.meritcap.repository.ScholarshipRepository;
import com.meritcap.service.ScholarshipService;
import org.springframework.stereotype.Service;

@Service
public class ScholarshipServiceImpl implements ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;

    public ScholarshipServiceImpl(ScholarshipRepository scholarshipRepository) {
        this.scholarshipRepository = scholarshipRepository;
    }

    @Override
    public ScholarshipResponseDto addScholarship(ScholarshipRequestDto scholarshipRequestDto) {
        return null;
    }
}
