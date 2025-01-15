package com.consultancy.education.repository.custom;

import com.consultancy.education.DTOs.requestDTOs.search.SearchCourseRequestDto;
import com.consultancy.education.DTOs.responseDTOs.collegeCourse.CollegeCourseResponseDto;
import com.consultancy.education.DTOs.responseDTOs.search.SearchCourseResponseDto;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeCourseRepositoryCustom {
    SearchCourseResponseDto<CollegeCourseResponseDto> searchCollegeCourses(SearchCourseRequestDto searchCourseRequestDto);
}
