package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.course.CourseRequestDto;
import com.meritcap.DTOs.responseDTOs.course.CourseResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {
    CourseResponseDto addCourse(CourseRequestDto courseRequestDto);

    List<CourseResponseDto> getAllCourses();

    Long getACourseCount();

    CourseResponseDto updateCourse(CourseRequestDto courseRequestDto, Long courseId);

    CourseResponseDto deleteCourse(Long courseId);

    List<CourseResponseDto> getCourseByName(String name);

    String bulkCoursesUpload(MultipartFile file);
}
