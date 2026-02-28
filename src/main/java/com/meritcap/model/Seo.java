package com.meritcap.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "seo") // Proper table name for SEO records
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Seo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "primary_keyword", nullable = false)
    String primaryKeyword;

    @Column(name = "keywords")
    String keywords;

    @Column(name = "meta_title", nullable = false)
    String metaTitle;

    @Column(name = "meta_description", nullable = false)
    String metaDescription;

    @Column(name = "featured_image")
    String featuredImage;

    @Column(name = "category")
    String category;

    @Column(name = "tags")
    String tags;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @OneToOne(mappedBy = "seo", cascade = CascadeType.ALL, orphanRemoval = true)
    Student student;

    @OneToOne(mappedBy = "seo", cascade = CascadeType.ALL, orphanRemoval = true)
    College college;

    @OneToOne(mappedBy = "seo", cascade = CascadeType.ALL, orphanRemoval = true)
    Blog blog;

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
