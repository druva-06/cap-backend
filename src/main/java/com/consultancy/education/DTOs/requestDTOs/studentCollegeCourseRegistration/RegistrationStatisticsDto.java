package com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationStatisticsDto {
    private long totalRegistrations;
    private long pending;
    private long submitted;
    private long approved;
    private long rejected;

    private List<StatusCount> byCollege;
    private List<StatusCount> byCourse;
    private List<YearCount> byIntakeYear;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusCount {
        private Long id;
        private String name;
        private long count;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class YearCount {
        private Integer year;
        private long count;
    }
}

