package com.meritcap.service.impl;

import com.meritcap.DTOs.requestDTOs.abroadExam.AbroadExamRequestDto;
import com.meritcap.DTOs.responseDTOs.abroadExam.AbroadExamResponseDto;
import com.meritcap.exception.NotFoundException;
import com.meritcap.model.AbroadExam;
import com.meritcap.model.Student;
import com.meritcap.repository.AbroadExamRepository;
import com.meritcap.repository.StudentRepository;
import com.meritcap.service.AbroadExamService;
import com.meritcap.transformer.AbroadExamTransformer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbroadExamServiceImpl implements AbroadExamService {

    AbroadExamRepository abroadExamRepository;
    StudentRepository studentRepository;

    public AbroadExamServiceImpl(AbroadExamRepository abroadExamRepository, StudentRepository studentRepository) {
        this.abroadExamRepository = abroadExamRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public AbroadExamResponseDto addAbroadExam(AbroadExamRequestDto abroadExamRequestDto, Long studentId) {
        if(studentRepository.findById(studentId).isPresent()){
            AbroadExam abroadExam =  AbroadExamTransformer.toEntity(abroadExamRequestDto);
            Student student = studentRepository.findById(studentId).get();
            abroadExam.setStudent(student);
            student.getAbroadExams().add(abroadExam);
            abroadExam = abroadExamRepository.save(abroadExam);
            return AbroadExamTransformer.toResDTO(abroadExam, student);
        }
        throw new NotFoundException("Student not found");
    }

    @Override
    public AbroadExamResponseDto updateAbroadExam(AbroadExamRequestDto abroadExamRequestDto, Long abroadExamId) {
        if(abroadExamRepository.findById(abroadExamId).isPresent()){
            AbroadExam abroadExam = abroadExamRepository.findById(abroadExamId).get();
            AbroadExamTransformer.updateAbroadExam(abroadExam, abroadExamRequestDto);
            abroadExam = abroadExamRepository.save(abroadExam);
            return AbroadExamTransformer.toResDTO(abroadExam, abroadExam.getStudent());
        }
        throw new NotFoundException("Exam details not found");
    }

    @Override
    public List<AbroadExamResponseDto> getAllStudentAbroadExams(Long studentId) {
        if(studentRepository.findById(studentId).isPresent()){
            Student student = studentRepository.findById(studentId).get();
            List<AbroadExam> abroadExams = student.getAbroadExams();
            return AbroadExamTransformer.toResDTO(abroadExams, student);
        }
        throw new NotFoundException("Student not found");
    }

    @Override
    public AbroadExamResponseDto deleteAbroadExam(Long abroadExamId) {
        if(abroadExamRepository.findById(abroadExamId).isPresent()){
            AbroadExam abroadExam = abroadExamRepository.findById(abroadExamId).get();
            AbroadExamResponseDto abroadExamResponseDto =  AbroadExamTransformer.toResDTO(abroadExam, abroadExam.getStudent());
            abroadExamRepository.delete(abroadExam);
            return abroadExamResponseDto;
        }
        throw new NotFoundException("Exam details not found");
    }
}
