package com.meritcap.repository;

import com.meritcap.model.College;
import com.meritcap.model.CollegeCourse;
import com.meritcap.model.Course;
import com.meritcap.repository.custom.CollegeCourseRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollegeCourseRepository extends JpaRepository<CollegeCourse, Long>, CollegeCourseRepositoryCustom {

//    @Query(value = """
//                SELECT DISTINCT cc.id as collegeCourseId, cc.college_id as collegeId, cc.course_id as courseId,  clg.name as collegeName, crs.name as courseName
//                FROM college_courses cc
//                INNER JOIN colleges clg ON clg.id = cc.college_id
//                INNER JOIN courses crs ON crs.id = cc.course_id
//                WHERE crs.name LIKE CONCAT('%', :term, '%')
//                """, nativeQuery = true)
//    List<Object[]> searchCollegeCourse(String term);

    boolean existsByCollegeAndCourse(College college, Course course);

    // optional: find by college id, course id
    boolean existsByCollegeIdAndCourseId(Long collegeId, Long courseId);
    Optional<CollegeCourse> findByCollegeIdAndCourseId(Long collegeId, Long courseId);


}
