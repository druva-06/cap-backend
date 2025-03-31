package com.consultancy.education.model;

import com.consultancy.education.enums.Gender;
import com.consultancy.education.enums.GraduationLevel;
import com.consultancy.education.enums.ActiveStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "students")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "graduation_level", nullable = false)
    GraduationLevel graduationLevel;

    @Column(name = "date_of_birth", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_status", nullable = false)
    ActiveStatus profileActiveStatus;

    @Column(name = "alternate_phone_number")
    String alternatePhoneNumber;

//    @Column(name = "profile_completion", nullable = false, columnDefinition = "INT DEFAULT 0")
//    Integer profileCompletion;
//
//    @Column(name = "aadhaar_number", nullable = false, unique = true, length = 12)
//    String aadhaarNumber;
//
//    @Column(name = "aadhaar_card_file", nullable = false)
//    String aadhaarCardFile;
//
//    @Column(name = "passport_number", nullable = false, unique = true)
//    String passportNumber;
//
//    @Column(name = "passport_file", nullable = false)
//    String passportFile;

    @JoinColumn
    @OneToOne
    Seo seo;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Address> addresses;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    List<StudentEducation> studentEducations;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    List<AbroadExam> abroadExams;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Certification> certifications;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Project> projects;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    Finance finance;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    Wishlist wishlist;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    AdmissionDocument admissionDocument;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    List<StudentCollegeCourseRegistration> studentCollegeCourseRegistrations;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    List<StudentEventRegistration> studentEventRegistrations;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Communication> communications;

}
