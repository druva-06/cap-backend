package com.meritcap.model;

import com.meritcap.enums.AddressType;
import com.meritcap.enums.EntityType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "address")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "address_line1", nullable = false)
    String addressLine1;

    @Column(name = "address_line2")
    String addressLine2;

    @Column(name = "city_village", nullable = false)
    String cityVillage;

    @Column(name = "pin_code", nullable = false)
    String pinCode;

    @Column(name = "landmark")
    String landmark;

    @Column(name = "state", nullable = false)
    String state;

    @Column(name = "country", nullable = false)
    String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false)
    AddressType addressType;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    EntityType entityType;

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
