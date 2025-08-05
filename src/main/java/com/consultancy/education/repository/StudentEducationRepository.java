package com.consultancy.education.repository;

import com.consultancy.education.model.StudentEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface StudentEducationRepository extends JpaRepository<StudentEducation, Long> {
    List<StudentEducation> findByStudentId(Long studentId);

    boolean existsByStudentIdAndEducationLevelAndInstitutionNameAndBoardAndStartYear(
            Long studentId,
            com.consultancy.education.enums.GraduationLevel educationLevel,
            String institutionName,
            String board,
            java.time.LocalDate startYear
    );
}
