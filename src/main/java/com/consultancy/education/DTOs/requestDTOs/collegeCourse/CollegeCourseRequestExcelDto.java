package com.consultancy.education.DTOs.requestDTOs.collegeCourse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CollegeCourseRequestExcelDto {

    String campusCode;

    String courseName;

    String department;

    String graduationLevel;

    String courseUrl;

    String duration;

    String intakeMonths;

    Integer intakeYear;

    String eligibilityCriteria;

    String applicationFee;

    String tuitionFee;

    Double ieltsMinScore;

    Double ieltsMinBandScore;

    Double toeflMinScore;

    Double toeflMinBandScore;

    Double pteMinScore;

    Double pteMinBandScore;

    Double detMinScore;

    Double greMinScore;

    Double gmatMinScore;

    Double satMinScore;

    Double catMinScore;

    Double min10thScore;

    Double minInterScore;

    Double minGraduationScore;

    String scholarshipEligible;

    String scholarshipDetails;

    String backlogAcceptanceRange;

    String remarks;
}
