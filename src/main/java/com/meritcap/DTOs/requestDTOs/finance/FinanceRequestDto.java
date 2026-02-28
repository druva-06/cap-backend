package com.meritcap.DTOs.requestDTOs.finance;

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
public class FinanceRequestDto {

    @NotNull(message = "Student budget is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Student budget must be greater than 0")
    Double studentBudget;

    @NotNull(message = "Loan support requirement status is mandatory")
    Boolean loanSupportRequired;

    @DecimalMin(value = "0.0", message = "Loan amount must be zero or positive")
    Double loanAmount;

    @Size(max = 255, message = "Loan bank name should not exceed 255 characters")
    String loanBank;

    @Size(max = 11, message = "Bank IFSC code should not exceed 11 characters")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format")
    String bankIfscCode;

    @Size(max = 255, message = "Bank branch name should not exceed 255 characters")
    String bankBranch;

    @Size(max = 50, message = "Loan term should not exceed 50 characters")
    String loanTerm;

    @DecimalMin(value = "0.0", inclusive = false, message = "Interest rate must be greater than 0")
    @DecimalMax(value = "100.0", message = "Interest rate must not exceed 100%")
    Double interestRate;

    @DecimalMin(value = "0.0", message = "Monthly instalment must be zero or positive")
    Double monthlyInstalment;

    Boolean collateralRequired;

    @Enumerated(EnumType.STRING)
    ApprovalStatus approvalStatus;

    @Size(max = 500, message = "Loan remark should not exceed 500 characters")
    String loanRemark;

    @PastOrPresent(message = "Loan approved date must be in the past or present")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate loanApprovedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate repaymentStartDate;

    @Size(max = 255, message = "Point of contact should not exceed 255 characters")
    String pointOfContact;

    @Size(max = 100, message = "Account owner relationship should not exceed 100 characters")
    String accountOwnerRelationship;
}
