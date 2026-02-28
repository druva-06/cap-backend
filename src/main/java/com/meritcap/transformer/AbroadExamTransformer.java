package com.meritcap.transformer;

import com.meritcap.DTOs.requestDTOs.abroadExam.AbroadExamRequestDto;
import com.meritcap.DTOs.responseDTOs.abroadExam.AbroadExamResponseDto;
import com.meritcap.model.AbroadExam;
import com.meritcap.model.Student;

import java.util.ArrayList;
import java.util.List;

public class AbroadExamTransformer {
    public static AbroadExam toEntity(AbroadExamRequestDto abroadExamRequestDto) {
        return AbroadExam.builder()
                .examType(abroadExamRequestDto.getExamType())
                .examTitle(abroadExamRequestDto.getExamTitle())
                .speakingScore(abroadExamRequestDto.getSpeakingScore())
                .readingScore(abroadExamRequestDto.getReadingScore())
                .writingScore(abroadExamRequestDto.getWritingScore())
                .listeningScore(abroadExamRequestDto.getListeningScore())
                .overallScore(abroadExamRequestDto.getOverallScore())
                .examDate(abroadExamRequestDto.getExamDate())
                .build();
    }

    public static AbroadExamResponseDto toResDTO(AbroadExam abroadExam, Student student) {
        return AbroadExamResponseDto.builder()
                .studentId(student.getId())
                .examId(abroadExam.getId())
                .examType(abroadExam.getExamType())
                .examTitle(abroadExam.getExamTitle())
                .speakingScore(abroadExam.getSpeakingScore())
                .readingScore(abroadExam.getReadingScore())
                .writingScore(abroadExam.getWritingScore())
                .listeningScore(abroadExam.getListeningScore())
                .overallScore(abroadExam.getOverallScore())
                .examDate(abroadExam.getExamDate())
                .build();
    }

    public static List<AbroadExamResponseDto> toResDTO(List<AbroadExam> abroadExam, Student student) {
        List<AbroadExamResponseDto> abroadExamResponseDtos = new ArrayList<>();
        for (AbroadExam abroadExamDto : abroadExam) {
            abroadExamResponseDtos.add(toResDTO(abroadExamDto, student));
        }
        return abroadExamResponseDtos;
    }

    public static void updateAbroadExam(AbroadExam abroadExam, AbroadExamRequestDto abroadExamRequestDto) {
        abroadExam.setExamType(abroadExamRequestDto.getExamType());
        abroadExam.setExamTitle(abroadExamRequestDto.getExamTitle());
        abroadExam.setSpeakingScore(abroadExamRequestDto.getSpeakingScore());
        abroadExam.setReadingScore(abroadExamRequestDto.getReadingScore());
        abroadExam.setWritingScore(abroadExamRequestDto.getWritingScore());
        abroadExam.setListeningScore(abroadExamRequestDto.getListeningScore());
        abroadExam.setOverallScore(abroadExamRequestDto.getOverallScore());
        abroadExam.setExamDate(abroadExamRequestDto.getExamDate());
    }
}
