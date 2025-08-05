package com.consultancy.education.service.impl;

import com.consultancy.education.DTOs.requestDTOs.student.StudentProfileRequestDto;
import com.consultancy.education.DTOs.requestDTOs.student.StudentProfileUpdateRequestDto;
import com.consultancy.education.DTOs.responseDTOs.student.StudentProfileResponseDto;
import com.consultancy.education.exception.AlreadyExistException;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.exception.ValidationException;
import com.consultancy.education.model.Student;
import com.consultancy.education.model.User;
import com.consultancy.education.repository.StudentRepository;
import com.consultancy.education.repository.UserRepository;
import com.consultancy.education.service.StudentProfileService;
import com.consultancy.education.transformer.StudentProfileTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Override
    public StudentProfileResponseDto addProfile(StudentProfileRequestDto requestDto) {
        log.info("Attempting to add profile for userId: {}", requestDto.getUserId());
        Student existing = studentRepository.findByUserId(requestDto.getUserId());
        if (existing != null) {
            log.warn("Profile already exists for userId: {}", requestDto.getUserId());
            throw new AlreadyExistException(Collections.singletonList("Profile already exists"));
        }

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found for userId: {}", requestDto.getUserId());
                    return new NotFoundException("User not found");
                });

        Student student = StudentProfileTransformer.toEntity(requestDto, user);
        student = studentRepository.save(student);
        log.info("Profile created for userId: {}", requestDto.getUserId());
        return StudentProfileTransformer.toResponseDto(student);
    }

    @Override
    public StudentProfileResponseDto getProfileByUserId(Long userId) {
        log.info("Fetching profile for userId: {}", userId);
        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            log.warn("Profile not found for userId: {}", userId);
            throw new NotFoundException("Student profile not found");
        }
        return StudentProfileTransformer.toResponseDto(student);
    }

    @Override
    public StudentProfileResponseDto updateProfile(Long userId, StudentProfileUpdateRequestDto requestDto) {
        log.info("Updating profile for userId: {}", userId);
        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            log.warn("Profile not found for update userId: {}", userId);
            throw new NotFoundException("Student profile not found");
        }
        StudentProfileTransformer.updateEntity(student, requestDto);
        student = studentRepository.save(student);
        log.info("Profile updated for userId: {}", userId);
        return StudentProfileTransformer.toResponseDto(student);
    }
}
