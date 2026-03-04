package com.meritcap.config;

import com.meritcap.model.BulkUploadJob;
import com.meritcap.repository.BulkUploadJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * On application startup, marks any orphaned IN_PROGRESS or PENDING bulk upload
 * jobs as FAILED. This handles the case where the pod crashes (e.g. OOM) while
 * processing a bulk upload — the job would otherwise stay stuck at IN_PROGRESS
 * forever.
 */
@Component
public class BulkUploadRecovery {

    private static final Logger log = LoggerFactory.getLogger(BulkUploadRecovery.class);

    private final BulkUploadJobRepository jobRepository;

    public BulkUploadRecovery(BulkUploadJobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void recoverOrphanedJobs() {
        recoverByStatus(BulkUploadJob.Status.IN_PROGRESS);
        recoverByStatus(BulkUploadJob.Status.PENDING);
    }

    private void recoverByStatus(BulkUploadJob.Status status) {
        List<BulkUploadJob> orphaned = jobRepository.findByStatus(status);
        if (orphaned.isEmpty()) {
            return;
        }
        log.warn("Found {} orphaned {} bulk upload job(s) — marking as FAILED", orphaned.size(), status);
        for (BulkUploadJob job : orphaned) {
            jobRepository.updateStatusAndError(
                    job.getId(),
                    BulkUploadJob.Status.FAILED,
                    "Server restarted while processing. Please retry the upload.",
                    LocalDateTime.now());
            log.warn("Marked orphaned job {} ({}) as FAILED", job.getId(), job.getFileName());
        }
    }
}
