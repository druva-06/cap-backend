package com.consultancy.education.repository;

import com.consultancy.education.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByReferenceTypeAndReferenceIdAndDocumentTypeAndIsDeletedFalse(String referenceType, Long referenceId, String documentType);
    List<Document> findAllByReferenceTypeAndReferenceIdAndIsDeletedFalse(String referenceType, Long referenceId);
}
