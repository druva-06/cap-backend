package com.consultancy.education.helper;

import com.consultancy.education.DTOs.requestDTOs.collegeCourse.CollegeCourseRequestExcelDto;
import com.consultancy.education.enums.GraduationLevel;
import com.consultancy.education.exception.ExcelException;
import com.consultancy.education.model.College;
import com.consultancy.education.model.Course;
import com.consultancy.education.utils.BasicValidations;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

public class ExcelHelper {

    public static boolean checkExcelFormat(MultipartFile multipartFile){
        String contentType = multipartFile.getContentType();
        if (contentType == null) return false;
        return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static Map<String, College> convertCollegeExcelIntoList(InputStream inputStream) throws Exception {
        Map<String, College> collegeMap = new HashMap<>();

        try{
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet collegeSheet = workbook.getSheet("colleges");
            Iterator<Row> colleges = collegeSheet.iterator();
            int row = 0;
            while(colleges.hasNext()){
                Row collegeRow = colleges.next();
                if(row == 0){
                    row++;
                    continue;
                }
                row++;
                College college = getCollege(collegeRow);
                if(college.getName() == null) break;
                collegeMap.put(college.getCampusCode(), college);
            }
        }
        catch (Exception e){
            throw new ExcelException(e.getMessage());
        }

        return collegeMap;
    }

    private static College getCollege(Row collegeRow) {
        BasicValidations basicValidations = new BasicValidations();
        int col = 0;
        College college = new College();
        for (Cell cell : collegeRow) {
            cell = collegeRow.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            switch (col){
                case 0:
                    college.setName(basicValidations.validateString(cell));
                    break;
                case 1:
                    college.setCampus(basicValidations.validateString(cell));
                    break;
                case 2:
                    college.setCampusCode(basicValidations.validateString(cell));
                    break;
                case 3:
                    college.setWebsiteUrl(basicValidations.validateString(cell));
                    break;
                case 4:
                    college.setCollegeLogo(basicValidations.validateString(cell));
                    break;
                case 5:
                    college.setCountry(basicValidations.validateString(cell));
                    break;
                case 6:
                    college.setEstablishedYear(basicValidations.validateInteger(cell));
                    break;
                case 7:
                    college.setRanking(basicValidations.validateString(cell));
                    break;
                case 8:
                    college.setDescription(basicValidations.validateString(cell));
                    break;
                case 9:
                    college.setCampusGalleryVideoLink(basicValidations.validateString(cell));
                    break;
                default:
                    break;
            }
            col++;
        }
        return college;
    }

    public static List<CollegeCourseRequestExcelDto> convertCollegeCourseExcelIntoList(InputStream inputStream) throws Exception {
        List<CollegeCourseRequestExcelDto> collegeCourseRequestExcelDtos = new ArrayList<>();

        try{
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet collegeSheet = workbook.getSheet("colleges");
            Iterator<Row> colleges = collegeSheet.iterator();
            int row = 0;
            while(colleges.hasNext()){
                Row collegeRow = colleges.next();
                if(row == 0){
                    row++;
                    continue;
                }
                row++;
                CollegeCourseRequestExcelDto collegeCourseRequestExcelDto = getCollegeCourse(collegeRow);
                if(collegeCourseRequestExcelDto.getCourseName() == null) break;
                collegeCourseRequestExcelDtos.add(collegeCourseRequestExcelDto);
            }
        }
        catch (Exception e){
            throw new ExcelException(e.getMessage());
        }

        return collegeCourseRequestExcelDtos;
    }

    public static List<Course> convertCourseExcelIntoList(InputStream inputStream) throws Exception {
        List<Course> courseArrayList = new ArrayList<>();

        try{
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            for(int i=0; i< workbook.getNumberOfSheets(); i++){
                XSSFSheet sheet = workbook.getSheetAt(i);
                Iterator<Row> courses = sheet.iterator();
                int row = 0;
                while(courses.hasNext()){
                    Row courseRow = courses.next();
                    if(row == 0){
                        row++;
                        continue;
                    }
                    row++;
                    Course course = getCourse(courseRow);
                    courseArrayList.add(course);
                }
            }
        }
        catch (Exception e){
            throw new ExcelException(e.getMessage());
        }

        return courseArrayList;
    }

    private static Course getCourse(Row courseRow) {
        BasicValidations basicValidations = new BasicValidations();
        int col = 0;
        Course course = new Course();
        for (Cell cell : courseRow) {
            cell = courseRow.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            switch (col){
                case 0:
                    course.setName(basicValidations.validateString(cell));
                    break;
                case 1:
                    course.setSpecialization(basicValidations.validateString(cell));
                    break;
                case 2:
                    course.setDepartment(basicValidations.validateString(cell));
                    break;
                case 3:
                    course.setGraduationLevel(GraduationLevel.valueOf(basicValidations.validateString(cell).toUpperCase()));
                    break;
                case 4:
                    course.setDescription(basicValidations.validateString(cell));
                    break;
                default:
                    break;
            }
            col++;
        }
        return course;
    }

    private static CollegeCourseRequestExcelDto getCollegeCourse(Row collegeRow) {
        BasicValidations basicValidations = new BasicValidations();
        CollegeCourseRequestExcelDto collegeCourseRequestExcelDto = new CollegeCourseRequestExcelDto();
        for (int col = 0; col < collegeRow.getLastCellNum(); col++) {
            Cell cell = collegeRow.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            switch (col){
                case 0:
                    collegeCourseRequestExcelDto.setCollegeName(basicValidations.validateString(cell));
                    break;
                case 1:
                    collegeCourseRequestExcelDto.setCampus(basicValidations.validateString(cell));
                    break;
                case 5:
                    collegeCourseRequestExcelDto.setCountry(basicValidations.validateString(cell));
                    break;
                case 10:
                    collegeCourseRequestExcelDto.setCourseName(basicValidations.validateString(cell));
                    break;
                case 11:
                    collegeCourseRequestExcelDto.setGraduationLevel(basicValidations.validateString(cell));
                    break;
                case 12:
                    collegeCourseRequestExcelDto.setCourseUrl(basicValidations.validateString(cell));
                    break;
                case 13:
                    collegeCourseRequestExcelDto.setDuration(basicValidations.validateString(cell));
                    break;
                case 14:
                    collegeCourseRequestExcelDto.setIntakeMonths(basicValidations.validateString(cell));
                    break;
                case 15:
                    collegeCourseRequestExcelDto.setIntakeYear(basicValidations.validateInteger(cell));
                    break;
                case 16:
                    collegeCourseRequestExcelDto.setEligibilityCriteria(basicValidations.validateString(cell));
                    break;
                case 17:
                    collegeCourseRequestExcelDto.setApplicationFee(basicValidations.validateString(cell));
                    break;
                case 18:
                    collegeCourseRequestExcelDto.setTuitionFee(basicValidations.validateString(cell));
                    break;
                case 19:
                    collegeCourseRequestExcelDto.setIeltsMinScore(basicValidations.validateDouble(cell));
                    break;
                case 20:
                    collegeCourseRequestExcelDto.setIeltsMinBandScore(basicValidations.validateDouble(cell));
                    break;
                case 21:
                    collegeCourseRequestExcelDto.setToeflMinScore(basicValidations.validateDouble(cell));
                    break;
                case 22:
                    collegeCourseRequestExcelDto.setToeflMinBandScore(basicValidations.validateDouble(cell));
                    break;
                case 23:
                    collegeCourseRequestExcelDto.setPteMinScore(basicValidations.validateDouble(cell));
                    break;
                case 24:
                    collegeCourseRequestExcelDto.setPteMinBandScore(basicValidations.validateDouble(cell));
                    break;
                case 25:
                    collegeCourseRequestExcelDto.setDetMinScore(basicValidations.validateDouble(cell));
                    break;
                case 26:
                    collegeCourseRequestExcelDto.setGreMinScore(basicValidations.validateDouble(cell));
                    break;
                case 27:
                    collegeCourseRequestExcelDto.setGmatMinScore(basicValidations.validateDouble(cell));
                    break;
                case 28:
                    collegeCourseRequestExcelDto.setSatMinScore(basicValidations.validateDouble(cell));
                    break;
                case 29:
                    collegeCourseRequestExcelDto.setCatMinScore(basicValidations.validateDouble(cell));
                    break;
                case 30:
                    collegeCourseRequestExcelDto.setMin10thScore(basicValidations.validateString(cell));
                    break;
                case 31:
                    collegeCourseRequestExcelDto.setMinInterScore(basicValidations.validateString(cell));
                    break;
                case 32:
                    collegeCourseRequestExcelDto.setMinGraduationScore(basicValidations.validateString(cell));
                    break;
                case 33:
                    collegeCourseRequestExcelDto.setScholarshipEligible(basicValidations.validateString(cell));
                    break;
                case 34:
                    collegeCourseRequestExcelDto.setScholarshipDetails(basicValidations.validateString(cell));
                    break;
                case 35:
                    collegeCourseRequestExcelDto.setBacklogAcceptanceRange(basicValidations.validateString(cell));
                    break;
                case 36:
                    collegeCourseRequestExcelDto.setRemarks(basicValidations.validateString(cell));
                    break;
                default:
                    break;
            }
        }
        return collegeCourseRequestExcelDto;
    }
}
