package com.meritcap.repository;

import com.meritcap.model.BulkUploadError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkUploadErrorRepository extends JpaRepository<BulkUploadError, Long> {
    // additional query methods can be added later if required
}

