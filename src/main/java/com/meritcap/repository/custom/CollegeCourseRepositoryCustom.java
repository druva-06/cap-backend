package com.meritcap.repository.custom;

import com.meritcap.DTOs.requestDTOs.search.SearchCourseRequestDto;
import com.meritcap.DTOs.responseDTOs.collegeCourse.CollegeCourseResponseDto;
import com.meritcap.DTOs.responseDTOs.collegeCourse.SearchCollegeCourseResponseDto;
import com.meritcap.DTOs.responseDTOs.search.SearchCourseResponseDto;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeCourseRepositoryCustom {
    SearchCourseResponseDto<SearchCollegeCourseResponseDto> searchCollegeCourses(SearchCourseRequestDto searchCourseRequestDto);
}
