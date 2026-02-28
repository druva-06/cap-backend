package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.student.StudentProfileRequestDto;
import com.meritcap.DTOs.requestDTOs.student.StudentProfileUpdateRequestDto;
import com.meritcap.DTOs.responseDTOs.student.StudentProfileResponseDto;

public interface StudentProfileService {
    StudentProfileResponseDto addProfile(StudentProfileRequestDto requestDto);
    StudentProfileResponseDto getProfileByUserId(Long userId);
    StudentProfileResponseDto updateProfile(Long userId, StudentProfileUpdateRequestDto requestDto);
}
