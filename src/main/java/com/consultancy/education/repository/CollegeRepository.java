package com.consultancy.education.repository;

import com.consultancy.education.model.College;
import com.consultancy.education.model.Course;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

    List<College> findByCampusCodeIn(Set<String> campusCodes);

    College findByNameAndCampusAndCountry(String name, String campus, String country);

    List<College> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT c FROM College c WHERE LOWER(CONCAT(c.name, c.campus, c.campusCode)) LIKE LOWER(:input)")
    List<College> searchByNameOrCampus(@Param("input") String input, PageRequest of);

    List<College> findAllByOrderByNameAsc();

    List<College> findAllByOrderByNameDesc();

    College findByCampusCode(String campusCode);
}
