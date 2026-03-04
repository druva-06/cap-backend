-- Migration: Create bulk upload tables
-- Date: 2025-03-05
-- Description: Tables for async bulk upload job tracking and error logging

CREATE TABLE IF NOT EXISTS bulk_upload_job (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    total_records INT NOT NULL DEFAULT 0,
    processed_records INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    error_message TEXT,
    created_by VARCHAR(100),
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    version INT DEFAULT 0,
    INDEX idx_bulk_upload_job_status (status),
    INDEX idx_bulk_upload_job_created_by (created_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS bulk_upload_errors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255),
    row_num INT,
    entity_type VARCHAR(50),
    identifier VARCHAR(255),
    raw_data LONGTEXT,
    error_message LONGTEXT NOT NULL,
    created_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
