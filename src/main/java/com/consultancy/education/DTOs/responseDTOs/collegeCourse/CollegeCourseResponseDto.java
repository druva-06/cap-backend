package com.consultancy.education.DTOs.responseDTOs.collegeCourse;

import com.consultancy.education.enums.CollegeCourseStatus;
import com.consultancy.education.enums.GraduationLevel;
import com.consultancy.education.enums.Month;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CollegeCourseResponseDto {
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
