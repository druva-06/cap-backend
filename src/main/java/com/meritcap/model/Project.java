package com.meritcap.model;

import com.meritcap.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "project_title", nullable = false)
    String projectTitle;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "project_duration", nullable = false)
    String projectDuration;

    @Column(name = "project_type")
    String projectType;

    @Column(name = "project_year")
    Integer projectYear;

    @Column(name = "technology", nullable = false)
    String technology;

    @Column(name = "role", nullable = false)
    String role;

    @Column(name = "project_files")
    String projectFiles;

    @Column(name = "production_url")
    String productionUrl;

    @Column(name = "repository_url")
    String repositoryUrl;

    @Column(name = "team_size", nullable = false)
    Integer teamSize;

    @Column(name = "status", nullable = false)
    ProjectStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @JoinColumn
    @ManyToOne
    Student student;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
