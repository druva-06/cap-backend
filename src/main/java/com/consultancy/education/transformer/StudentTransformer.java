package com.consultancy.education.transformer;

import com.consultancy.education.DTOs.requestDTOs.student.StudentRequestDto;
import com.consultancy.education.DTOs.requestDTOs.student.StudentUpdateRequestDto;
import com.consultancy.education.DTOs.responseDTOs.student.StudentResponseDto;
import com.consultancy.education.model.Student;

public class StudentTransformer {

//    public static Student toEntity(StudentRequestDto studentRequestDto) {
//        return Student.builder()
//                .username(studentRequestDto.getUsername())
//                .email(studentRequestDto.getEmail())
//                .password(studentRequestDto.getPassword())
//                .firstName(studentRequestDto.getFirstName())
//                .lastName(studentRequestDto.getLastName())
//                .phoneNumber(studentRequestDto.getPhoneNumber())
//                .alternatePhoneNumber(studentRequestDto.getAlternatePhoneNumber())
//                .dateOfBirth(studentRequestDto.getBirthDate())
//                .gender(studentRequestDto.getGender())
//                .graduationLevel(studentRequestDto.getGraduationLevel())
//                .profileActiveStatus(studentRequestDto.getProfileActiveStatus())
//                .profileCompletion(studentRequestDto.getProfileCompletion())
//                .profileImage(studentRequestDto.getProfileImage())
//                .aadhaarNumber(studentRequestDto.getAadhaarNumber())
//                .aadhaarCardFile(studentRequestDto.getAadhaarCardFile())
//                .passportNumber(studentRequestDto.getPassportNumber())
//                .passportFile(studentRequestDto.getPassportFile())
//                .build();
//    }
//
//    public static StudentResponseDto toResDTO(Student student) {
//        return StudentResponseDto.builder()
//                .id(student.getId())
//                .username(student.getUsername())
//                .build();
//    }
//
//    public static void updateStudentDetails(Student student, StudentUpdateRequestDto studentRequestDto) {
//        student.setUsername(studentRequestDto.getUsername());
//        student.setEmail(studentRequestDto.getEmail());
//        student.setFirstName(studentRequestDto.getFirstName());
//        student.setLastName(studentRequestDto.getLastName());
//        student.setPhoneNumber(studentRequestDto.getPhoneNumber());
//        student.setAlternatePhoneNumber(studentRequestDto.getAlternatePhoneNumber());
//        student.setDateOfBirth(studentRequestDto.getBirthDate());
//        student.setGender(studentRequestDto.getGender());
//        student.setGraduationLevel(studentRequestDto.getGraduationLevel());
//        student.setProfileActiveStatus(studentRequestDto.getProfileActiveStatus());
//        student.setProfileCompletion(studentRequestDto.getProfileCompletion());
//        student.setProfileImage(studentRequestDto.getProfileImage());
//        student.setAadhaarNumber(studentRequestDto.getAadhaarNumber());
//        student.setAadhaarCardFile(studentRequestDto.getAadhaarCardFile());
//        student.setPassportNumber(studentRequestDto.getPassportNumber());
//        student.setPassportFile(studentRequestDto.getPassportFile());
//    }
}
