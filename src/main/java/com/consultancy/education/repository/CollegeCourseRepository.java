package com.consultancy.education.repository;

import com.consultancy.education.model.CollegeCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollegeCourseRepository extends JpaRepository<CollegeCourse, Long> {

    @Query(value = """ 
                SELECT DISTINCT cc.id as collegeCourseId, cc.college_id as collegeId, cc.course_id as courseId,  clg.name as collegeName, crs.name as courseName
                FROM college_courses cc
                INNER JOIN colleges clg ON clg.id = cc.college_id
                INNER JOIN courses crs ON crs.id = cc.course_id
                WHERE crs.name LIKE CONCAT('%', :term, '%')
                """, nativeQuery = true)
    List<Object[]> searchCollegeCourse(String term);
}
