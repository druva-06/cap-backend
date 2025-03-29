package com.consultancy.education.DTOs.responseDTOs.collegeCourse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SearchCollegeCourseResponseDto {
    Long collegeCourseId;
    Long collegeId;
    String collegeName;
    Long courseId;
    String courseName;
    String campusCode;
    String campusName;
    String country;
    String graduationLevel;
    String collegeImage;
    Integer intakeYear;
    String tuitionFee;
    Integer establishedYear;
    String intakeMonths;
}
