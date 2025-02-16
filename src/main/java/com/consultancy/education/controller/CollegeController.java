package com.consultancy.education.controller;

import com.consultancy.education.DTOs.requestDTOs.college.CollegeAndAddressRequestDto;
import com.consultancy.education.DTOs.requestDTOs.college.CollegeRequestDto;
import com.consultancy.education.DTOs.responseDTOs.college.CollegeResponseDto;
import com.consultancy.education.DTOs.responseDTOs.collegeCourse.CollegeCourseResponseDto;
import com.consultancy.education.exception.CollegeException;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.exception.ValidationException;
import com.consultancy.education.response.ApiFailureResponse;
import com.consultancy.education.response.ApiSuccessResponse;
import com.consultancy.education.helper.ExcelHelper;
import com.consultancy.education.service.CollegeService;
import com.consultancy.education.utils.ToMap;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/college")
public class CollegeController {

    private final CollegeService collegeService;

    CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCollege(@RequestBody @Valid CollegeRequestDto collegeRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiFailureResponse<>(ToMap.bindingResultToMap(bindingResult), "Validation Failed", 400));
        }
        try{
            CollegeResponseDto collegeResponseDto = collegeService.add(collegeRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiSuccessResponse<>(collegeResponseDto,"College created successfully", 201));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
        }
    }

    @PostMapping("/bulkCollegesUpload")
    public ResponseEntity<?> bulkCollegesUpload(@RequestParam("file") MultipartFile file){
        if(ExcelHelper.checkExcelFormat(file)) {
            try{
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiSuccessResponse<>(collegeService.bulkCollegesUpload(file), "Success!", 201));
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiFailureResponse<>(new ArrayList<>(),"Incorrect excel format", 400));
    }

    @GetMapping("/getColleges")
    public ResponseEntity<?> getColleges(){
        try{
            List<CollegeResponseDto> colleges = collegeService.getColleges();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(colleges, "Colleges fetched successfully", 200));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
        }
    }

    @GetMapping("/getByCountries")
    public ResponseEntity<?> getCollegesByCountries(@RequestParam List<String> countries){
        try{
            List<CollegeResponseDto> colleges = collegeService.getCollegesByCountries(countries);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(colleges, "Colleges filtered successfully", 200));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
        }
    }

    @GetMapping("/getByName")
    public ResponseEntity<?> getCollegeByName(@RequestParam String name){
        try{
            List<CollegeResponseDto> collegeResponseDto = collegeService.getCollegeByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(collegeResponseDto, "College fetched successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
        }
    }

    @GetMapping("/getCollege")
    public ResponseEntity<?> getCollege(@RequestParam Long id){
        try{
            CollegeRequestDto collegeRequestDto = collegeService.getCollege(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(collegeRequestDto, "College fetched successfully", 200));
        }
        catch (CollegeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
        }
    }

    @GetMapping("/getCount")
    public ResponseEntity<?> getCollegeCount(){
        try {
            Long collegesCount = collegeService.getCollegeCount();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(Map.of("collegesCount", collegesCount), "Colleges count fetched successfully", 200));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCollege(@RequestParam Long id){
        try{
            CollegeResponseDto collegeResponseDto  = collegeService.deleteCollege(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(collegeResponseDto, "College deleted successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCollege(@PathVariable Long id, @RequestBody CollegeRequestDto collegeRequestDto){
        try{
            CollegeRequestDto collegeResponseDto = collegeService.updateCollege(id, collegeRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(collegeResponseDto, "College updated successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
        }
    }

    @GetMapping("/sortByName")
    public ResponseEntity<?> sortCollegeByName(@RequestParam String type){
        try{
            List<CollegeResponseDto> collegeResponseDto = collegeService.sortCollegeByName(type);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(collegeResponseDto, "Colleges sorted successfully", 200));
        }
        catch (ValidationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiFailureResponse<>(e.getErrors(),e.getMessage(), 400));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
        }
    }

//    @GetMapping("/getCollegeCourses/{collegeId}")
//    public ResponseEntity<?> getCollegeCourses(@PathVariable Long collegeId){
//        try{
//            List<CollegeCourseResponseDto> collegeCourseResponseDtos = collegeService.getCollegeCourses(collegeId);
//            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(collegeCourseResponseDtos, "College courses fetched successfully", 200));
//        }
//        catch (NotFoundException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 404));
//        }
//        catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
//        }
//    }

    @GetMapping("updateInternalCollegeData")
    public ResponseEntity<?> updateCollegeData(){
        try{
            String result = collegeService.updateInternalCollegeData();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(result, "College campus updated successfully", 200));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(),e.getMessage(), 500));
        }

    }
}
