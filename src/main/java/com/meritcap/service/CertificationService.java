package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.certification.CertificationRequestDto;
import com.meritcap.DTOs.responseDTOs.certification.CertificationResponseDto;

import java.util.List;

public interface CertificationService {
    CertificationResponseDto addCertification(CertificationRequestDto certificationRequestDto, Long studentId);

    CertificationResponseDto updateCertification(CertificationRequestDto certificationRequestDto, Long id);

    List<CertificationResponseDto> getAllCertifications(Long studentId);

    CertificationResponseDto getCertification(Long id);

    CertificationResponseDto deleteCertification(Long id);
}
