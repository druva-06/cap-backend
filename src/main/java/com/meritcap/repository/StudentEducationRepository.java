package com.meritcap.repository;

import com.meritcap.model.StudentEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface StudentEducationRepository extends JpaRepository<StudentEducation, Long> {
    List<StudentEducation> findByStudentId(Long studentId);

    boolean existsByStudentIdAndEducationLevelAndInstitutionName(
            Long studentId,
            com.meritcap.enums.GraduationLevel educationLevel,
            String institutionName
    );
}
