package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.college.CollegeCreateRequestDto;
import com.meritcap.DTOs.responseDTOs.college.CollegeResponseDto;

public interface CollegeService {
    CollegeResponseDto create(CollegeCreateRequestDto req);
    CollegeResponseDto getById(Long id);
}
