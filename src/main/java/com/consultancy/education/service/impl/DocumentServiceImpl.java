package com.consultancy.education.service.impl;

import com.consultancy.education.DTOs.requestDTOs.document.DocumentUploadRequestDto;
import com.consultancy.education.DTOs.responseDTOs.document.DocumentResponseDto;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.exception.ValidationException;
import com.consultancy.education.model.Document;
import com.consultancy.education.repository.DocumentRepository;
import com.consultancy.education.repository.StudentRepository;
import com.consultancy.education.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final StudentRepository studentRepository;
    private S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Value("${aws.s3.accessKeyId}")
    private String accessKeyId;
    @Value("${aws.s3.secretAccessKey}")
    private String secretAccessKey;

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "application/pdf", "image/jpeg", "image/png", "image/jpg",
            "video/mp4", "audio/mpeg"
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public DocumentServiceImpl(DocumentRepository documentRepository, StudentRepository studentRepository) {
        this.documentRepository = documentRepository;
        this.studentRepository = studentRepository;
    }

    @PostConstruct
    public void initS3Client() {
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_2)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                ))
                .build();
    }

    @Override
    public DocumentResponseDto uploadDocument(DocumentUploadRequestDto requestDto, MultipartFile file, String uploadedBy) {
        log.info("Upload document request for referenceType={}, referenceId={}, documentType={}, uploadedBy={}",
                requestDto.getReferenceType(), requestDto.getReferenceId(), requestDto.getDocumentType(), uploadedBy);

        // --- Validations ---
        if (file == null || file.isEmpty()) {
            log.warn("Empty file upload attempt");
            throw new ValidationException(Collections.singletonList("File must not be empty"));
        }
        if (!ALLOWED_MIME_TYPES.contains(file.getContentType())) {
            log.warn("Unsupported file type: {}", file.getContentType());
            throw new ValidationException(Collections.singletonList("Unsupported file type: " + file.getContentType()));
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            log.warn("File size too large: {} bytes", file.getSize());
            throw new ValidationException(Collections.singletonList("File size exceeds 10MB limit"));
        }
        if (requestDto.getReferenceType() == null || requestDto.getReferenceType().isBlank()) {
            throw new ValidationException(Collections.singletonList("Reference type is required"));
        }
        if (requestDto.getReferenceId() == null) {
            throw new ValidationException(Collections.singletonList("Reference id is required"));
        }
        if (requestDto.getDocumentType() == null || requestDto.getDocumentType().isBlank()) {
            throw new ValidationException(Collections.singletonList("Document type is required"));
        }
        // Reference existence validation (student only, extend as needed)
        if ("STUDENT".equalsIgnoreCase(requestDto.getReferenceType()) &&
                !studentRepository.existsById(requestDto.getReferenceId())) {
            log.warn("Student not found for ID: {}", requestDto.getReferenceId());
            throw new NotFoundException("Student not found");
        }

        // --- Always Replace: Mark previous as deleted ---
        documentRepository.findByReferenceTypeAndReferenceIdAndDocumentTypeAndIsDeletedFalse(
                        requestDto.getReferenceType(), requestDto.getReferenceId(), requestDto.getDocumentType())
                .ifPresent(existingDoc -> {
                    existingDoc.setIsDeleted(true);
                    documentRepository.save(existingDoc);
                });

        // --- Upload File to S3 ---
        String fileExtension = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf('.'));
        String fileKey = String.format("%s/%d/%s/%s_%d%s",
                requestDto.getReferenceType().toLowerCase(),
                requestDto.getReferenceId(),
                requestDto.getDocumentType().toLowerCase(),
                requestDto.getDocumentType().toLowerCase(),
                System.currentTimeMillis(),
                fileExtension
        );

        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try {
            file.transferTo(tempFile);
            s3Client.putObject(
                    PutObjectRequest.builder().bucket(bucketName).key(fileKey).build(),
                    RequestBody.fromFile(tempFile)
            );
        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new RuntimeException("Error while uploading document");
        }

        String fileUrl = s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(fileKey)).toExternalForm();

        // --- Save document record ---
        Document document = Document.builder()
                .referenceType(requestDto.getReferenceType())
                .referenceId(requestDto.getReferenceId())
                .documentType(requestDto.getDocumentType())
                .fileUrl(fileUrl)
                .uploadedBy(uploadedBy)
                .isDeleted(false)
                .build();
        document = documentRepository.save(document);
        log.info("Document uploaded and saved with id={}", document.getId());

        return DocumentResponseDto.builder()
                .id(document.getId())
                .referenceType(document.getReferenceType())
                .referenceId(document.getReferenceId())
                .documentType(document.getDocumentType())
                .fileUrl(document.getFileUrl())
                .uploadedBy(document.getUploadedBy())
                .uploadedAt(document.getUploadedAt())
                .build();
    }

    @Override
    public List<DocumentResponseDto> getDocuments(String referenceType, Long referenceId) {
        log.info("Fetching documents for referenceType={}, referenceId={}", referenceType, referenceId);
        List<Document> docs = documentRepository.findAllByReferenceTypeAndReferenceIdAndIsDeletedFalse(referenceType, referenceId);
        List<DocumentResponseDto> responseDtos = new ArrayList<>();
        for (Document doc : docs) {
            responseDtos.add(DocumentResponseDto.builder()
                    .id(doc.getId())
                    .referenceType(doc.getReferenceType())
                    .referenceId(doc.getReferenceId())
                    .documentType(doc.getDocumentType())
                    .fileUrl(doc.getFileUrl())
                    .uploadedBy(doc.getUploadedBy())
                    .uploadedAt(doc.getUploadedAt())
                    .build());
        }
        return responseDtos;
    }

    @Override
    public void deleteDocument(Long documentId, String requestedBy) {
        log.info("Delete document request for id={}, by={}", documentId, requestedBy);
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document not found"));
        doc.setIsDeleted(true);
        documentRepository.save(doc);
        log.info("Document marked as deleted: {}", documentId);
    }
}
