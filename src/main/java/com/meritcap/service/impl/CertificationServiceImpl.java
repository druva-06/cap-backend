package com.meritcap.service.impl;

import com.meritcap.DTOs.requestDTOs.certification.CertificationRequestDto;
import com.meritcap.DTOs.responseDTOs.certification.CertificationResponseDto;
import com.meritcap.exception.NotFoundException;
import com.meritcap.model.Certification;
import com.meritcap.model.Student;
import com.meritcap.repository.CertificationRepository;
import com.meritcap.repository.StudentRepository;
import com.meritcap.service.CertificationService;
import com.meritcap.transformer.CertificationTransformer;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CertificationServiceImpl implements CertificationService {

    private final CertificationRepository certificationRepository;
    private final StudentRepository studentRepository;

    public CertificationServiceImpl(CertificationRepository certificationRepository, StudentRepository studentRepository) {
        this.certificationRepository = certificationRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public CertificationResponseDto addCertification(CertificationRequestDto certificationRequestDto, Long studentId) {
        if(studentRepository.findById(studentId).isPresent()){
            Student student = studentRepository.findById(studentId).get();
            Certification certification = CertificationTransformer.toEntity(certificationRequestDto);
            certification.setStudent(student);
            student.getCertifications().add(certification);
            certification = certificationRepository.save(certification);
            return CertificationTransformer.toResDTO(certification, student);
        }
        throw new NotFoundException("Student not found");
    }

    @Override
    public CertificationResponseDto updateCertification(CertificationRequestDto certificationRequestDto, Long id) {
        if(certificationRepository.findById(id).isPresent()){
            Certification certification = certificationRepository.findById(id).get();
            CertificationTransformer.updateCertification(certification, certificationRequestDto);
            certification = certificationRepository.save(certification);
            return CertificationTransformer.toResDTO(certification, certification.getStudent());
        }
        throw new NotFoundException("Certification not found");
    }

    @Override
    public List<CertificationResponseDto> getAllCertifications(Long studentId) {
        if(studentRepository.findById(studentId).isPresent()){
            List<Certification> certifications = studentRepository.findById(studentId).get().getCertifications();
            Student student = studentRepository.findById(studentId).get();
            return CertificationTransformer.toResDTO(certifications, student);
        }
        throw new NotFoundException("Student not found");
    }

    @Override
    public CertificationResponseDto getCertification(Long id) {
        if(certificationRepository.findById(id).isPresent()){
            Certification certification = certificationRepository.findById(id).get();
            return CertificationTransformer.toResDTO(certification, certification.getStudent());
        }
        throw new NotFoundException("Certification not found");
    }

    @Override
    public CertificationResponseDto deleteCertification(Long id) {
        if(certificationRepository.findById(id).isPresent()){
            Certification certification = certificationRepository.findById(id).get();
            certificationRepository.delete(certification);
            return CertificationTransformer.toResDTO(certification, certification.getStudent());
        }
        throw new NotFoundException("Certification not found");
    }
}
