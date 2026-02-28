package com.meritcap.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.*;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "event_title", nullable = false)
    String eventTitle;

    @Column(name = "event_short_description")
    String eventShortDescription;

    @Lob
    @Column(name = "event_long_description")
    String eventLongDescription;

    @Column(name = "event_image")
    String eventImage;

    @Column(name = "event_capacity")
    Integer eventCapacity;

    @Column(name = "event_fee")
    Double eventFee;

    @Column(name = "event_date", nullable = false)
    LocalDate eventDate;

    @Column(name = "event_time")
    LocalTime eventTime;

    @Column(name = "registration_deadline")
    LocalDate registrationDeadline;

    @Column(name = "is_virtual")
    Boolean isVirtual;

    @Column(name = "virtual_link")
    String virtualLink;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<SpeakerEventRegistration> speakerEventRegistrations;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<StudentEventRegistration> studentEventRegistrations;

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
