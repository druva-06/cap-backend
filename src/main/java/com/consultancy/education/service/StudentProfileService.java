package com.consultancy.education.service;

import com.consultancy.education.DTOs.requestDTOs.student.StudentProfileRequestDto;
import com.consultancy.education.DTOs.requestDTOs.student.StudentProfileUpdateRequestDto;
import com.consultancy.education.DTOs.responseDTOs.student.StudentProfileResponseDto;

public interface StudentProfileService {
    StudentProfileResponseDto addProfile(StudentProfileRequestDto requestDto);
    StudentProfileResponseDto getProfileByUserId(Long userId);
    StudentProfileResponseDto updateProfile(Long userId, StudentProfileUpdateRequestDto requestDto);
}
