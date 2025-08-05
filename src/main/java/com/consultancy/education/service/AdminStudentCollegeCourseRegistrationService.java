package com.consultancy.education.service;

import com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration.AddRemarksRequestDto;
import com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration.AssignCounselorRequestDto;
import com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration.RegistrationDecisionRequestDto;
import com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration.RegistrationStatisticsDto;
import com.consultancy.education.DTOs.responseDTOs.studentCollegeCourseRegistration.StudentCollegeCourseRegistrationResponseDto;
import java.util.List;

public interface AdminStudentCollegeCourseRegistrationService {
    List<StudentCollegeCourseRegistrationResponseDto> listRegistrations(
            String status,
            Long studentId,
            Long collegeId,
            Long courseId,
            Integer intakeYear,
            String dateFrom,
            String dateTo
    );

    StudentCollegeCourseRegistrationResponseDto getRegistrationById(Long registrationId);

    StudentCollegeCourseRegistrationResponseDto assignCounselor(AssignCounselorRequestDto requestDto);

    StudentCollegeCourseRegistrationResponseDto decideOnRegistration(RegistrationDecisionRequestDto requestDto);

    StudentCollegeCourseRegistrationResponseDto addRemarks(AddRemarksRequestDto requestDto);

    RegistrationStatisticsDto getStatistics();
}
