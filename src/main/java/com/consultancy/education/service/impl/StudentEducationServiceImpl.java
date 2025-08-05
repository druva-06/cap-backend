package com.consultancy.education.service.impl;

import com.consultancy.education.DTOs.requestDTOs.studentEducation.StudentEducationRequestDto;
import com.consultancy.education.DTOs.responseDTOs.studentEducation.StudentEducationResponseDto;
import com.consultancy.education.DTOs.responseDTOs.document.DocumentResponseDto;
import com.consultancy.education.enums.DocumentType;
import com.consultancy.education.exception.AlreadyExistException;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.model.Document;
import com.consultancy.education.model.Student;
import com.consultancy.education.model.StudentEducation;
import com.consultancy.education.repository.DocumentRepository;
import com.consultancy.education.repository.StudentEducationRepository;
import com.consultancy.education.repository.StudentRepository;
import com.consultancy.education.service.StudentEducationService;
import com.consultancy.education.transformer.StudentEducationTransformer;
import com.consultancy.education.transformer.DocumentTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StudentEducationServiceImpl implements StudentEducationService {

    private final StudentEducationRepository studentEducationRepository;
    private final StudentRepository studentRepository;
    private final DocumentRepository documentRepository;

    public StudentEducationServiceImpl(StudentEducationRepository studentEducationRepository,
                                       StudentRepository studentRepository,
                                       DocumentRepository documentRepository) {
        this.studentEducationRepository = studentEducationRepository;
        this.studentRepository = studentRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    @Transactional
    public StudentEducationResponseDto addStudentEducation(StudentEducationRequestDto dto, Long userId) {
        log.info("Adding student education for user: {}", userId);

        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            log.error("Student not found for userId: {}", userId);
            throw new NotFoundException("Student not found");
        }

        boolean exists = studentEducationRepository.existsByStudentIdAndEducationLevelAndInstitutionNameAndBoardAndStartYear(
                student.getId(), dto.getEducationLevel(), dto.getInstitutionName(), dto.getBoard(), dto.getStartYear());

        if (exists) {
            log.warn("Duplicate education detected for userId: {}", userId);
            throw new AlreadyExistException(List.of("Duplicate education record exists"));
        }

        StudentEducation education = StudentEducationTransformer.toEntity(dto);
        education.setStudent(student);
        StudentEducation saved = studentEducationRepository.save(education);

        log.info("Student education added: {}", saved.getId());
        return StudentEducationTransformer.toResDTO(saved, null);
    }

    @Override
    @Transactional
    public StudentEducationResponseDto updateStudentEducation(StudentEducationRequestDto dto, Long educationId) {
        log.info("Updating student education: {}", educationId);

        StudentEducation education = studentEducationRepository.findById(educationId)
                .orElseThrow(() -> new NotFoundException("Education record not found"));

        StudentEducationTransformer.updateStudentEducation(education, dto);
        studentEducationRepository.save(education);

        log.info("Student education updated: {}", educationId);
        return StudentEducationTransformer.toResDTO(education,
                education.getCertificateDocument() != null
                        ? DocumentTransformer.toDto(education.getCertificateDocument())
                        : null
        );
    }

    @Override
    @Transactional
    public void deleteStudentEducation(Long educationId) {
        log.info("Deleting student education: {}", educationId);

        StudentEducation education = studentEducationRepository.findById(educationId)
                .orElseThrow(() -> new NotFoundException("Education record not found"));
        // Optionally delete the linked certificate document
        if (education.getCertificateDocument() != null) {
            documentRepository.delete(education.getCertificateDocument());
            log.info("Deleted associated certificate document: {}", education.getCertificateDocument().getId());
        }
        studentEducationRepository.delete(education);
        log.info("Student education deleted: {}", educationId);
    }

    @Override
    public List<StudentEducationResponseDto> getStudentEducation(Long userId) {
        log.info("Fetching education records for user: {}", userId);

        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            log.error("Student not found for userId: {}", userId);
            throw new NotFoundException("Student not found");
        }
        List<StudentEducation> list = studentEducationRepository.findByStudentId(student.getId());
        return list.stream()
                .map(e -> StudentEducationTransformer.toResDTO(e,
                        e.getCertificateDocument() != null
                                ? DocumentTransformer.toDto(e.getCertificateDocument())
                                : null))
                .toList();
    }

    @Override
    @Transactional
    public void attachCertificate(Long educationId, Long documentId) {
        log.info("Attaching certificate (documentId={}) to educationId={}", documentId, educationId);

        StudentEducation education = studentEducationRepository.findById(educationId)
                .orElseThrow(() -> new NotFoundException("Education record not found"));
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document not found"));
        // Always replace
        education.setCertificateDocument(document);
        studentEducationRepository.save(education);

        log.info("Certificate attached to educationId={}", educationId);
    }
}
