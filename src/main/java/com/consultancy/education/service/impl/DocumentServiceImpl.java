package com.consultancy.education.service.impl;

import com.consultancy.education.DTOs.requestDTOs.document.DocumentUploadRequestDto;
import com.consultancy.education.DTOs.responseDTOs.document.DocumentResponseDto;
import com.consultancy.education.enums.DocumentStatus;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.exception.ValidationException;
import com.consultancy.education.model.Document;
import com.consultancy.education.repository.DocumentRepository;
import com.consultancy.education.repository.StudentRepository;
import com.consultancy.education.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import jakarta.annotation.PostConstruct;

import java.io.InputStream;
import java.time.Instant;
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
    @Transactional
    public DocumentResponseDto uploadDocument(DocumentUploadRequestDto requestDto, MultipartFile file, String uploadedBy) {
        log.info("Received upload request: referenceType={}, referenceId={}, documentType={}, category={}, uploadedBy={}",
                requestDto.getReferenceType(), requestDto.getReferenceId(), requestDto.getDocumentType(),
                requestDto.getCategory(), uploadedBy);

        // Validate inputs
        validateRequest(requestDto, file);

        String contentType = Optional.ofNullable(file.getContentType()).orElse("application/octet-stream");
        long fileSize = file.getSize();
        log.debug("File meta - originalName='{}', contentType='{}', sizeBytes={}",
                file.getOriginalFilename(), contentType, fileSize);

        // Validate reference existence for STUDENT (adjust for other types if needed)
        if ("STUDENT".equalsIgnoreCase(requestDto.getReferenceType())) {
            boolean exists = studentRepository.existsById(requestDto.getReferenceId());
            if (!exists) {
                log.warn("Student not found: id={}", requestDto.getReferenceId());
                throw new NotFoundException("Student not found");
            }
            log.debug("Student exists: id={}", requestDto.getReferenceId());
        }

        // Soft-delete any existing active document for same reference/documentType
        documentRepository.findByReferenceTypeAndReferenceIdAndDocumentTypeAndIsDeletedFalse(
                requestDto.getReferenceType(),
                requestDto.getReferenceId(),
                requestDto.getDocumentType()
        ).ifPresent(existing -> {
            log.info("Found existing active document id={}; marking as deleted", existing.getId());
            existing.setIsDeleted(true);
            documentRepository.save(existing);
            log.debug("Existing document id={} marked deleted", existing.getId());
        });

        // Build safe S3 key
        String originalName = Optional.ofNullable(file.getOriginalFilename()).orElse("file");
        String extension = "";
        int lastDot = originalName.lastIndexOf('.');
        if (lastDot >= 0 && lastDot < originalName.length() - 1) {
            extension = originalName.substring(lastDot);
        }
        String sanitizedReferenceType = sanitizePathComponent(requestDto.getReferenceType());
        String sanitizedDocumentType = sanitizePathComponent(requestDto.getDocumentType());
        String fileKey = String.format("%s/%d/%s/%s_%d%s",
                sanitizedReferenceType,
                requestDto.getReferenceId(),
                sanitizedDocumentType,
                sanitizedDocumentType,
                System.currentTimeMillis(),
                extension
        );
        log.debug("Constructed S3 fileKey='{}'", fileKey);

        // Stream upload to S3
        try (InputStream in = file.getInputStream()) {
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .contentType(contentType)
                    .contentLength(fileSize)
                    .build();

            log.info("Uploading to S3. bucket='{}' key='{}'", bucketName, fileKey);
            s3Client.putObject(putReq, RequestBody.fromInputStream(in, fileSize));
            log.info("S3 upload successful. key='{}'", fileKey);
        } catch (Exception e) {
            log.error("Failed to upload file to S3. referenceType={}, referenceId={}, documentType={}, key={}",
                    requestDto.getReferenceType(), requestDto.getReferenceId(), requestDto.getDocumentType(), fileKey, e);
            throw new RuntimeException("Error while uploading document to S3", e);
        }

        // Build file URL (uses S3 utilities)
        String fileUrl = s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(fileKey)).toExternalForm();
        log.debug("Resolved S3 file URL='{}' for key='{}'", fileUrl, fileKey);

        // Create Document entity
        Document document = Document.builder()
                .referenceType(requestDto.getReferenceType())
                .referenceId(requestDto.getReferenceId())
                .documentType(requestDto.getDocumentType())
                .category(requestDto.getCategory())
                .remarks(requestDto.getRemarks())
                .documentStatus(DocumentStatus.PENDING)
                .fileUrl(fileUrl)
                .uploadedBy(uploadedBy)
                .isDeleted(false)
                .build();

        // Persist entity. If DB save fails, attempt S3 cleanup
        try {
            document = documentRepository.save(document);
            log.info("Document saved to DB with id={}", document.getId());
        } catch (Exception dbEx) {
            log.error("DB save failed after S3 upload. Attempting to delete S3 object key='{}' to avoid orphan", fileKey, dbEx);
            try {
                s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(fileKey).build());
                log.info("Deleted uploaded S3 object key='{}' after DB failure", fileKey);
            } catch (Exception delEx) {
                // cleanup best-effort: log but don't mask original exception
                log.error("Failed to delete S3 object key='{}' after DB failure; manual cleanup may be required", fileKey, delEx);
            }
            throw dbEx;
        }

        // Build response DTO
        DocumentResponseDto response = DocumentResponseDto.builder()
                .id(document.getId())
                .referenceType(document.getReferenceType())
                .referenceId(document.getReferenceId())
                .documentType(document.getDocumentType())
                .category(document.getCategory())
                .remarks(document.getRemarks())
                .documentStatus(document.getDocumentStatus().toString())
                .fileUrl(document.getFileUrl())
                .uploadedBy(document.getUploadedBy())
                .uploadedAt(document.getUploadedAt() != null ? document.getUploadedAt() : Instant.now())
                .build();

        log.info("Upload flow complete: documentId={}, referenceType={}, referenceId={}",
                response.getId(), response.getReferenceType(), response.getReferenceId());

        return response;
    }

    // -------------------------
    // Helper validations & utils
    // -------------------------

    private void validateRequest(DocumentUploadRequestDto requestDto, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("Validation failed: file missing/empty");
            throw new ValidationException(Collections.singletonList("File must not be empty"));
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            log.warn("Validation failed: unsupported content-type='{}'", contentType);
            throw new ValidationException(Collections.singletonList("Unsupported file type: " + contentType));
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            log.warn("Validation failed: file size {} exceeds max {}", file.getSize(), MAX_FILE_SIZE);
            throw new ValidationException(Collections.singletonList("File size exceeds " + MAX_FILE_SIZE + " bytes limit"));
        }

        if (requestDto.getReferenceType() == null || requestDto.getReferenceType().isBlank()) {
            log.warn("Validation failed: referenceType missing");
            throw new ValidationException(Collections.singletonList("Reference type is required"));
        }

        if (requestDto.getReferenceId() == null) {
            log.warn("Validation failed: referenceId missing");
            throw new ValidationException(Collections.singletonList("Reference id is required"));
        }

        if (requestDto.getDocumentType() == null || requestDto.getDocumentType().isBlank()) {
            log.warn("Validation failed: documentType missing");
            throw new ValidationException(Collections.singletonList("Document type is required"));
        }

        if (requestDto.getCategory() == null || requestDto.getCategory().isBlank()) {
            log.warn("Validation failed: category missing");
            throw new ValidationException(Collections.singletonList("Category is required"));
        }
    }

    /**
     * Sanitize a path component to be safe for S3 keys (lowercase alphanum + hyphen).
     */
    private String sanitizePathComponent(String input) {
        if (input == null) return "unknown";
        return input.toLowerCase().replaceAll("[^a-z0-9\\-]", "_");
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
                    .remarks(doc.getRemarks())
                    .category(doc.getCategory())
                    .documentStatus(doc.getDocumentStatus().toString())
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
