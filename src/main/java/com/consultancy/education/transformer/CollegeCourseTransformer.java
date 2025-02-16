package com.consultancy.education.transformer;


import com.consultancy.education.DTOs.requestDTOs.collegeCourse.CollegeCourseRequestExcelDto;
import com.consultancy.education.model.CollegeCourse;
import com.consultancy.education.utils.FormatConverter;

public class CollegeCourseTransformer {

    public static CollegeCourse excelToEntity(CollegeCourseRequestExcelDto collegeCourseRequestExcelDto) {
        return CollegeCourse.builder()
                .courseUrl(collegeCourseRequestExcelDto.getCourseUrl())
                .duration(FormatConverter.cnvrtDurationToInteger(collegeCourseRequestExcelDto.getDuration())) // Need to Change
                .intakeMonths(FormatConverter.cnvrtIntakesToList(collegeCourseRequestExcelDto.getIntakeMonths())) // Need to Change
                .intakeYear(collegeCourseRequestExcelDto.getIntakeYear())
                .eligibilityCriteria(collegeCourseRequestExcelDto.getEligibilityCriteria())
                .applicationFee(collegeCourseRequestExcelDto.getApplicationFee())
                .tuitionFee(collegeCourseRequestExcelDto.getTuitionFee())
                .ieltsMinScore(collegeCourseRequestExcelDto.getIeltsMinScore())
                .ieltsMinBandScore(collegeCourseRequestExcelDto.getIeltsMinBandScore())
                .toeflMinScore(collegeCourseRequestExcelDto.getToeflMinScore())
                .toeflMinBandScore(collegeCourseRequestExcelDto.getToeflMinBandScore())
                .pteMinScore(collegeCourseRequestExcelDto.getPteMinScore())
                .pteMinBandScore(collegeCourseRequestExcelDto.getPteMinBandScore())
                .detMinScore(collegeCourseRequestExcelDto.getDetMinScore())
                .greMinScore(collegeCourseRequestExcelDto.getGreMinScore())
                .gmatMinScore(collegeCourseRequestExcelDto.getGmatMinScore())
                .satMinScore(collegeCourseRequestExcelDto.getSatMinScore())
                .catMinScore(collegeCourseRequestExcelDto.getCatMinScore())
                .min10thScore(collegeCourseRequestExcelDto.getMin10thScore())
                .minInterScore(collegeCourseRequestExcelDto.getMinInterScore())
                .minGraduationScore(collegeCourseRequestExcelDto.getMinGraduationScore())
                .scholarshipEligible(collegeCourseRequestExcelDto.getScholarshipEligible())
                .scholarshipDetails(collegeCourseRequestExcelDto.getScholarshipDetails())
                .backlogAcceptanceRange(collegeCourseRequestExcelDto.getBacklogAcceptanceRange())
                .remarks(collegeCourseRequestExcelDto.getRemarks())
                .build();
    }

//    public static CollegeCourse toEntity(CollegeCourseRequestExcelDto collegeCourseRequestExcelDto) {
//        return CollegeCourse.builder()
//                .courseUrl(collegeCourseRequestExcelDto.getCourseUrl())
//                .duration(collegeCourseRequestExcelDto.getDuration())
//
//                .build();
//    }
//
//    public static CollegeCourseResponseDto toResDto(CollegeCourse collegeCourse, Long collegeCourseId, String collegeName, String courseName) {
//        return CollegeCourseResponseDto.builder()
//                .collegeCourseId(collegeCourseId)
//                .collegeName(collegeName)
//                .courseName(courseName)
//                //.intakeMonth(collegeCourse.getIntakeMonth())
//                .intakeYear(collegeCourse.getIntakeYear())
//                .tuitionFee(collegeCourse.getTuitionFee())
//                .applicationFee(collegeCourse.getApplicationFee())
//                .duration(collegeCourse.getDuration())
//                .applicationDeadline(collegeCourse.getApplicationDeadline())
//                .maxStudents(collegeCourse.getMaxStudents())
//                .status(collegeCourse.getStatus())
//                .build();
//    }
//
//    public static List<CollegeCourseResponseDto> toResDto(List<CollegeCourse> collegeCourses) {
//        List<CollegeCourseResponseDto> collegeCourseResponseDtos = new ArrayList<>();
//        for (CollegeCourse collegeCourse : collegeCourses) {
//            collegeCourseResponseDtos.add(toResDto(collegeCourse, collegeCourse.getId(), collegeCourse.getCollege().getName(), collegeCourse.getCourse().getName()));
//        }
//        return collegeCourseResponseDtos;
//    }
//
//    public static void updateCollegeCourse(CollegeCourse collegeCourse, CollegeCourseRequestExcelDto collegeCourseRequestExcelDto) {
//        //collegeCourse.setIntakeMonth(collegeCourseRequestDto.getIntakeMonth());
//        collegeCourse.setIntakeYear(collegeCourseRequestExcelDto.getIntakeYear());
//        collegeCourse.setTuitionFee(collegeCourseRequestExcelDto.getTuitionFee());
//        collegeCourse.setApplicationFee(collegeCourseRequestExcelDto.getApplicationFee());
//        collegeCourse.setDuration(collegeCourseRequestExcelDto.getDuration());
//        collegeCourse.setApplicationDeadline(collegeCourseRequestExcelDto.getApplicationDeadline());
//        collegeCourse.setMaxStudents(collegeCourseRequestExcelDto.getMaxStudents());
//        collegeCourse.setStatus(collegeCourseRequestExcelDto.getStatus());
//    }
}
