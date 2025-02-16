package com.consultancy.education.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "colleges")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(name = "campus_name")
    String campus;

    @Column(name = "campus_code", unique = true, nullable = false)
    String campusCode;

    @Column(name = "website_url")
    String websiteUrl;

    @Column(name = "college_logo")
    String collegeLogo;

    @Column(name = "country")
    String country;

    @Column(name = "established_year")
    Integer establishedYear;

    @Column(name = "ranking")
    String ranking;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "campus_gallery_video_link")
    String campusGalleryVideoLink;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @JoinColumn
    @OneToOne
    Seo seo;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CollegeCourse> collegeCourses = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        this.campusCode = UUID.randomUUID().toString();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
