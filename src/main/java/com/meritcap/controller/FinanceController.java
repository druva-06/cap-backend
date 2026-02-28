package com.meritcap.controller;

import com.meritcap.DTOs.requestDTOs.finance.FinanceRequestDto;
import com.meritcap.DTOs.responseDTOs.finance.FinanceResponseDto;
import com.meritcap.exception.NotFoundException;
import com.meritcap.response.ApiFailureResponse;
import com.meritcap.response.ApiSuccessResponse;
import com.meritcap.service.FinanceService;
import com.meritcap.utils.ToMap;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/finance")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFinance(@RequestBody @Valid FinanceRequestDto financeRequestDto, BindingResult bindingResult, @RequestParam Long studentId) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiFailureResponse<>(ToMap.bindingResultToMap(bindingResult), "Validation failed", 400));
        }
        try{
            FinanceResponseDto financeResponseDto = financeService.addFinance(financeRequestDto, studentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiSuccessResponse<>(financeResponseDto, "finance details added successfully", 201));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getFinance(@RequestParam Long studentId) {
        try{
            FinanceResponseDto financeResponseDto = financeService.getFinance(studentId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(financeResponseDto, "finance details fetched successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateFinance(@RequestBody @Valid FinanceRequestDto financeRequestDto, BindingResult bindingResult, @RequestParam Long studentId) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiFailureResponse<>(ToMap.bindingResultToMap(bindingResult), "Validation failed", 400));
        }
        try{
            FinanceResponseDto financeResponseDto = financeService.updateFinance(financeRequestDto, studentId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(financeResponseDto, "finance details updated successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @PatchMapping("/updateFinanceStatus")
    public ResponseEntity<?> updateFinanceStatus(@RequestParam Long studentId, @RequestParam String status) {
        try{
            FinanceResponseDto financeResponseDto = financeService.updateFinanceStatus(studentId, status);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(financeResponseDto, "finance status updated successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFinance(@RequestParam Long studentId) {
        try{
            FinanceResponseDto financeResponseDto = financeService.deleteFinance(studentId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(financeResponseDto, "finance details deleted successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }
}