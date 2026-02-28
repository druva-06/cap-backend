package com.meritcap.transformer;

import com.meritcap.DTOs.requestDTOs.student.StudentProfileRequestDto;
import com.meritcap.DTOs.requestDTOs.student.StudentProfileUpdateRequestDto;
import com.meritcap.DTOs.responseDTOs.student.StudentProfileResponseDto;
import com.meritcap.model.Student;
import com.meritcap.model.User;

public class StudentProfileTransformer {

    public static Student toEntity(StudentProfileRequestDto dto, User user) {
        return Student.builder()
                .user(user)
                .alternatePhoneNumber(dto.getAlternatePhoneNumber())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .graduationLevel(dto.getGraduationLevel())
                .profileActiveStatus(dto.getProfileActiveStatus())
                .profileCompletion(0) // Start at 0, update as needed
                .build();
    }

    public static void updateEntity(Student student, StudentProfileUpdateRequestDto dto) {
        student.setAlternatePhoneNumber(dto.getAlternatePhoneNumber());
        student.setGender(dto.getGender());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setGraduationLevel(dto.getGraduationLevel());
        student.setProfileActiveStatus(dto.getProfileActiveStatus());
        // Optionally update profileCompletion here
    }

    public static StudentProfileResponseDto toResponseDto(Student student) {
        return StudentProfileResponseDto.builder()
                .userId(student.getUser().getId())
                .alternatePhoneNumber(student.getAlternatePhoneNumber())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .graduationLevel(student.getGraduationLevel())
                .profileActiveStatus(student.getProfileActiveStatus())
                .profileCompletion(student.getProfileCompletion())
                .build();
    }
}
