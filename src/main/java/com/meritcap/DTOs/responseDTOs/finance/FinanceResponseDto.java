package com.meritcap.DTOs.responseDTOs.finance;

import com.meritcap.enums.ApprovalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FinanceResponseDto {
    Long studentId;
    Long financeId;
    Double studentBudget;
    Boolean loanSupportRequired;
    Double loanAmount;
    String loanBank;
    String bankIfscCode;
    String bankBranch;
    String loanTerm;
    Double interestRate;
    Double monthlyInstalment;
    Boolean collateralRequired;
    ApprovalStatus approvalStatus;
    String loanRemark;
    LocalDate loanApprovedDate;
    LocalDate repaymentStartDate;
    String pointOfContact;
    String accountOwnerRelationship;
}
