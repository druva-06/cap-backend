package com.consultancy.education.service;

import com.consultancy.education.DTOs.requestDTOs.studentEducation.StudentEducationRequestDto;
import com.consultancy.education.DTOs.responseDTOs.studentEducation.StudentEducationResponseDto;

import java.util.List;

public interface StudentEducationService {
    StudentEducationResponseDto addStudentEducation(StudentEducationRequestDto dto, Long userId);
    StudentEducationResponseDto updateStudentEducation(StudentEducationRequestDto dto, Long educationId);
    void deleteStudentEducation(Long educationId);
    List<StudentEducationResponseDto> getStudentEducation(Long userId);
    void attachCertificate(Long educationId, Long documentId);
}
