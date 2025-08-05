package com.consultancy.education.repository;

import com.consultancy.education.model.StudentCollegeCourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCollegeCourseRegistrationRepository extends JpaRepository<StudentCollegeCourseRegistration, Long> {
    Optional<StudentCollegeCourseRegistration> findByStudentIdAndCollegeCourseSnapshot_CourseIdAndIntakeSessionAndApplicationYear(
            Long studentId, Long courseId, String intakeSession, Integer applicationYear
    );
    List<StudentCollegeCourseRegistration> findAllByStudentId(Long studentId);
}

