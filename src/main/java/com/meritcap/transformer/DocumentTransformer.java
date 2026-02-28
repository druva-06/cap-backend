package com.meritcap.transformer;

import com.meritcap.DTOs.responseDTOs.document.DocumentResponseDto;
import com.meritcap.model.Document;

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
