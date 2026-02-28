package com.meritcap.transformer;

import com.meritcap.DTOs.requestDTOs.studentEducation.StudentEducationRequestDto;
import com.meritcap.DTOs.responseDTOs.document.DocumentResponseDto;
import com.meritcap.DTOs.responseDTOs.studentEducation.StudentEducationResponseDto;
import com.meritcap.model.StudentEducation;

public class StudentEducationTransformer {
    public static StudentEducation toEntity(StudentEducationRequestDto dto) {
        return StudentEducation.builder()
                .educationLevel(dto.getEducationLevel())
                .institutionName(dto.getInstitutionName())
                .board(dto.getBoard())
                .collegeCode(dto.getCollegeCode())
                .institutionAddress(dto.getInstitutionAddress())
                .startYear(dto.getStartYear())
                .endYear(dto.getEndYear())
                .percentage(dto.getPercentage())
                .cgpa(dto.getCgpa())
                .fieldOfStudy(dto.getFieldOfStudy())
                .degree(dto.getDegree())
                .backlogs(dto.getBacklogs())
                .build();
    }

    public static void updateStudentEducation(StudentEducation education, StudentEducationRequestDto dto) {
        education.setEducationLevel(dto.getEducationLevel());
        education.setInstitutionName(dto.getInstitutionName());
        education.setBoard(dto.getBoard());
        education.setCollegeCode(dto.getCollegeCode());
        education.setInstitutionAddress(dto.getInstitutionAddress());
        education.setStartYear(dto.getStartYear());
        education.setEndYear(dto.getEndYear());
        education.setPercentage(dto.getPercentage());
        education.setCgpa(dto.getCgpa());
        education.setFieldOfStudy(dto.getFieldOfStudy());
        education.setDegree(dto.getDegree());
        education.setBacklogs(dto.getBacklogs());
    }

    public static StudentEducationResponseDto toResDTO(StudentEducation education, DocumentResponseDto docDto) {
        return StudentEducationResponseDto.builder()
                .educationId(education.getId())
                .educationLevel(education.getEducationLevel())
                .institutionName(education.getInstitutionName())
                .board(education.getBoard())
                .collegeCode(education.getCollegeCode())
                .institutionAddress(education.getInstitutionAddress())
                .startYear(education.getStartYear())
                .endYear(education.getEndYear())
                .percentage(education.getPercentage())
                .cgpa(education.getCgpa())
                .fieldOfStudy(education.getFieldOfStudy())
                .degree(education.getDegree())
                .backlogs(education.getBacklogs())
                .certificateDocument(docDto)
                .build();
    }
}
