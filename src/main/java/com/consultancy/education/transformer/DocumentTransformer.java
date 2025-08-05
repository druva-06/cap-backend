package com.consultancy.education.transformer;

import com.consultancy.education.DTOs.responseDTOs.document.DocumentResponseDto;
import com.consultancy.education.model.Document;

public class DocumentTransformer {

    public static DocumentResponseDto toDto(Document document) {
        if (document == null) return null;
        return DocumentResponseDto.builder()
                .id(document.getId())
                .documentType(document.getDocumentType())
                .fileUrl(document.getFileUrl())
                .uploadedBy(document.getUploadedBy())
                .uploadedAt(document.getUploadedAt())
                .build();
    }
}
