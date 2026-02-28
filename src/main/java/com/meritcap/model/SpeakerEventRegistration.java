package com.meritcap.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "speaker_event_registrations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpeakerEventRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "registration_date", nullable = false)
    LocalDate registrationDate;

    @Column(name = "speaker_role", nullable = false)
    String speakerRole;

    @Column(name = "compensation_details")
    String compensationDetails;

    @Column(name = "presentation_topic", nullable = false)
    String presentationTopic;

    @Column(name = "presentation_file")
    String presentationFile;

    @Column(name = "travel_accommodation")
    Boolean travelAccommodation;

    @Column(name = "travel_details")
    String travelDetails;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @JoinColumn
    @ManyToOne
    Event event;

    @JoinColumn
    @ManyToOne
    User user;

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
