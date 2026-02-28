package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.finance.FinanceRequestDto;
import com.meritcap.DTOs.responseDTOs.finance.FinanceResponseDto;
import jakarta.validation.Valid;

public interface FinanceService {
    FinanceResponseDto addFinance(FinanceRequestDto financeRequestDto, Long studentId);

    FinanceResponseDto updateFinance(FinanceRequestDto financeRequestDto, Long studentId);

    FinanceResponseDto getFinance(Long studentId);

    FinanceResponseDto deleteFinance(Long studentId);

    FinanceResponseDto updateFinanceStatus(Long studentId, String status);
}
