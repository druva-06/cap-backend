package com.meritcap.transformer;

import com.meritcap.DTOs.requestDTOs.certification.CertificationRequestDto;
import com.meritcap.DTOs.responseDTOs.certification.CertificationResponseDto;
import com.meritcap.model.Certification;
import com.meritcap.model.Student;

import java.util.ArrayList;
import java.util.List;

public class CertificationTransformer {

    public static Certification toEntity(CertificationRequestDto certificationRequestDto) {
        return Certification.builder()
                .certificationTitle(certificationRequestDto.getCertificationTitle())
                .type(certificationRequestDto.getType())
                .certifiedBy(certificationRequestDto.getCertifiedBy())
                .level(certificationRequestDto.getLevel())
                .issueDate(certificationRequestDto.getIssueDate())
                .expirationDate(certificationRequestDto.getExpirationDate())
                .description(certificationRequestDto.getDescription())
                .certificateFile(certificationRequestDto.getCertificateFile())
                .build();
    }

    public static CertificationResponseDto toResDTO(Certification certification, Student student) {
        return CertificationResponseDto.builder()
                .certificationId(certification.getId())
                .studentId(student.getId())
                .certificationTitle(certification.getCertificationTitle())
                .type(certification.getType())
                .certifiedBy(certification.getCertifiedBy())
                .level(certification.getLevel())
                .issueDate(certification.getIssueDate())
                .expirationDate(certification.getExpirationDate())
                .description(certification.getDescription())
                .certificateFile(certification.getCertificateFile())
                .build();
    }

    public static List<CertificationResponseDto> toResDTO(List<Certification> certifications, Student student) {
        List<CertificationResponseDto> certificationResponseDtos = new ArrayList<>();

        for (Certification certification : certifications) {
            certificationResponseDtos.add(toResDTO(certification, student));
        }
        return certificationResponseDtos;
    }

    public static void updateCertification(Certification certification, CertificationRequestDto certificationRequestDto) {
        certification.setCertificationTitle(certificationRequestDto.getCertificationTitle());
        certification.setType(certificationRequestDto.getType());
        certification.setCertifiedBy(certificationRequestDto.getCertifiedBy());
        certification.setLevel(certificationRequestDto.getLevel());
        certification.setIssueDate(certificationRequestDto.getIssueDate());
        certification.setExpirationDate(certificationRequestDto.getExpirationDate());
        certification.setDescription(certificationRequestDto.getDescription());
        certification.setCertificateFile(certificationRequestDto.getCertificateFile());
    }
}
