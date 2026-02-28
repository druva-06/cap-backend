package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.scholarship.ScholarshipRequestDto;
import com.meritcap.DTOs.responseDTOs.scholarship.ScholarshipResponseDto;

public interface ScholarshipService {
    ScholarshipResponseDto addScholarship(ScholarshipRequestDto scholarshipRequestDto);
}
