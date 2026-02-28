package com.meritcap.transformer;

import com.meritcap.DTOs.responseDTOs.studentCollegeCourseRegistration.StudentCollegeCourseRegistrationResponseDto;
import com.meritcap.model.CollegeCourse;
import com.meritcap.model.CollegeCourseSnapshot;
import com.meritcap.model.StudentCollegeCourseRegistration;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class StudentCollegeCourseRegistrationTransformer {

    public static StudentCollegeCourseRegistrationResponseDto toResDto(StudentCollegeCourseRegistration reg) {
        CollegeCourseSnapshot snap = reg.getCollegeCourseSnapshot();
        return StudentCollegeCourseRegistrationResponseDto.builder()
                .registrationId(reg.getId())
                .studentId(reg.getStudent().getId())
                .collegeCourseSnapshotId(snap.getId())
                .intakeSession(reg.getIntakeSession())
                .applicationYear(reg.getApplicationYear())
                .status(reg.getApplicationStatus().name())
                .remarks(reg.getRemarks())
                .createdAt(reg.getCreatedAt())
                .updatedAt(reg.getUpdatedAt())
                .courseName(snap.getCourseName())
                .collegeName(snap.getCollegeName())
                .build();
    }
}
