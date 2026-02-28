package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.studentCollegeCourseRegistration.StudentCollegeCourseRegistrationEditRequestDto;
import com.meritcap.DTOs.requestDTOs.studentCollegeCourseRegistration.StudentCollegeCourseRegistrationRequestDto;
import com.meritcap.DTOs.responseDTOs.studentCollegeCourseRegistration.StudentCollegeCourseRegistrationResponseDto;

import java.util.List;

public interface StudentCollegeCourseRegistrationService {

    StudentCollegeCourseRegistrationResponseDto registerStudentForCourse(StudentCollegeCourseRegistrationRequestDto request);

    StudentCollegeCourseRegistrationResponseDto editRegistration(StudentCollegeCourseRegistrationEditRequestDto requestDto);

    StudentCollegeCourseRegistrationResponseDto getRegistrationById(Long registrationId);

    List<StudentCollegeCourseRegistrationResponseDto> getRegistrationsByStudentId(Long studentId);

    StudentCollegeCourseRegistrationResponseDto submitRegistration(Long registrationId);
}
