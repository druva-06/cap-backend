package com.consultancy.education.service;

import com.consultancy.education.DTOs.requestDTOs.document.DocumentUploadRequestDto;
import com.consultancy.education.DTOs.responseDTOs.document.DocumentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    DocumentResponseDto uploadDocument(DocumentUploadRequestDto requestDto, MultipartFile file, String uploadedBy);
    List<DocumentResponseDto> getDocuments(String referenceType, Long referenceId);
    void deleteDocument(Long documentId, String requestedBy);
    DocumentResponseDto uploadProfileImage(MultipartFile file, String uploadedBy);
}
