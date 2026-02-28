package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.abroadExam.AbroadExamRequestDto;
import com.meritcap.DTOs.responseDTOs.abroadExam.AbroadExamResponseDto;

import java.util.List;

public interface AbroadExamService {
    AbroadExamResponseDto addAbroadExam(AbroadExamRequestDto abroadExamRequestDto, Long studentId);

    AbroadExamResponseDto updateAbroadExam(AbroadExamRequestDto abroadExamRequestDto, Long abroadExamId);

    List<AbroadExamResponseDto> getAllStudentAbroadExams(Long studentId);

    AbroadExamResponseDto deleteAbroadExam(Long abroadExamId);
}
