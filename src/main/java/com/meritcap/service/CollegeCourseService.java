package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.search.SearchCourseRequestDto;
import com.meritcap.DTOs.responseDTOs.collegeCourse.CollegeCourseResponseDto;
import com.meritcap.DTOs.responseDTOs.collegeCourse.SearchCollegeCourseResponseDto;
import com.meritcap.DTOs.responseDTOs.search.SearchCourseResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface CollegeCourseService {
//    String bulkCollegeCourseUpload(MultipartFile file);

    SearchCourseResponseDto<SearchCollegeCourseResponseDto> getCollegeCourses(SearchCourseRequestDto searchCourseRequestDto);

    CollegeCourseResponseDto getCollegeCourseDetail(Long collegeCourseId);

//    CollegeCourseResponseDto addCollegeCourse(CollegeCourseRequestExcelDto collegeCourseRequestExcelDto, Long collegeId, Long courseId);
//
//    CollegeCourseResponseDto updateCollegeCourse(@Valid CollegeCourseRequestExcelDto collegeCourseRequestExcelDto, Long collegeCourseId);
//
//    CollegeCourseResponseDto deleteCollegeCourse(Long collegeCourseId);
}
