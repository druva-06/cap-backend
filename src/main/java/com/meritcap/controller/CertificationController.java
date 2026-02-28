package com.meritcap.controller;

import com.meritcap.DTOs.requestDTOs.certification.CertificationRequestDto;
import com.meritcap.DTOs.responseDTOs.certification.CertificationResponseDto;
import com.meritcap.exception.NotFoundException;
import com.meritcap.response.ApiFailureResponse;
import com.meritcap.response.ApiSuccessResponse;
import com.meritcap.service.CertificationService;
import com.meritcap.utils.ToMap;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/certification")
public class CertificationController {

    private final CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCertification(@RequestBody @Valid CertificationRequestDto certificationRequestDto, BindingResult bindingResult, @RequestParam Long studentId) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiFailureResponse<>(ToMap.bindingResultToMap(bindingResult), "Validation failed", 400));
        }
        try{
            CertificationResponseDto certificationResponseDto = certificationService.addCertification(certificationRequestDto, studentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiSuccessResponse<>(certificationResponseDto, "certification added successfully", 201));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCertification(@RequestBody @Valid CertificationRequestDto certificationRequestDto, @PathVariable Long id, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiFailureResponse<>(new ArrayList<>(), "Validation failed", 400));
        }
        try{
            CertificationResponseDto certificationResponseDto = certificationService.updateCertification(certificationRequestDto, id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(certificationResponseDto, "certification updated successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getCertification(@PathVariable Long id){
        try{
            CertificationResponseDto certificationResponseDto = certificationService.getCertification(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(certificationResponseDto, "certification fetched successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCertification(@PathVariable Long id){
        try{
            CertificationResponseDto certificationResponseDto = certificationService.deleteCertification(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(certificationResponseDto, "certification deleted successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCertifications(@RequestParam Long studentId) {
        try{
            List<CertificationResponseDto> certificationResponseDto = certificationService.getAllCertifications(studentId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(certificationResponseDto, "certifications fetched successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }
}
