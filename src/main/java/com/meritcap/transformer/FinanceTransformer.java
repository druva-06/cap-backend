package com.meritcap.transformer;

import com.meritcap.DTOs.requestDTOs.finance.FinanceRequestDto;
import com.meritcap.DTOs.responseDTOs.finance.FinanceResponseDto;
import com.meritcap.model.Finance;
import com.meritcap.model.Student;

public class FinanceTransformer {

    public static Finance toEntity(FinanceRequestDto financeRequestDto) {
        return Finance.builder()
                .studentBudget(financeRequestDto.getStudentBudget())
                .loanSupportRequired(financeRequestDto.getLoanSupportRequired())
                .loanAmount(financeRequestDto.getLoanAmount())
                .loanBank(financeRequestDto.getLoanBank())
                .bankIfscCode(financeRequestDto.getBankIfscCode())
                .bankBranch(financeRequestDto.getBankBranch())
                .loanTerm(financeRequestDto.getLoanTerm())
                .interestRate(financeRequestDto.getInterestRate())
                .monthlyInstalment(financeRequestDto.getMonthlyInstalment())
                .collateralRequired(financeRequestDto.getCollateralRequired())
                .approvalStatus(financeRequestDto.getApprovalStatus())
                .loanRemark(financeRequestDto.getLoanRemark())
                .loanApprovedDate(financeRequestDto.getLoanApprovedDate())
                .repaymentStartDate(financeRequestDto.getRepaymentStartDate())
                .pointOfContact(financeRequestDto.getPointOfContact())
                .accountOwnerRelationship(financeRequestDto.getAccountOwnerRelationship())
                .build();
    }

    public static FinanceResponseDto toResDTO(Finance finance, Student student) {
        return FinanceResponseDto.builder()
                .financeId(finance.getId())
                .studentId(student.getId())
                .studentBudget(finance.getStudentBudget())
                .loanSupportRequired(finance.getLoanSupportRequired())
                .loanAmount(finance.getLoanAmount())
                .loanBank(finance.getLoanBank())
                .bankIfscCode(finance.getBankIfscCode())
                .bankBranch(finance.getBankBranch())
                .loanTerm(finance.getLoanTerm())
                .interestRate(finance.getInterestRate())
                .monthlyInstalment(finance.getMonthlyInstalment())
                .collateralRequired(finance.getCollateralRequired())
                .approvalStatus(finance.getApprovalStatus())
                .loanRemark(finance.getLoanRemark())
                .loanApprovedDate(finance.getLoanApprovedDate())
                .repaymentStartDate(finance.getRepaymentStartDate())
                .pointOfContact(finance.getPointOfContact())
                .accountOwnerRelationship(finance.getAccountOwnerRelationship())
                .build();
    }

    public static void updateFinance(Finance finance, FinanceRequestDto financeRequestDto) {
        finance.setStudentBudget(financeRequestDto.getStudentBudget());
        finance.setLoanSupportRequired(financeRequestDto.getLoanSupportRequired());
        finance.setLoanAmount(financeRequestDto.getLoanAmount());
        finance.setLoanBank(financeRequestDto.getLoanBank());
        finance.setBankIfscCode(financeRequestDto.getBankIfscCode());
        finance.setBankBranch(financeRequestDto.getBankBranch());
        finance.setLoanTerm(financeRequestDto.getLoanTerm());
        finance.setInterestRate(financeRequestDto.getInterestRate());
        finance.setApprovalStatus(financeRequestDto.getApprovalStatus());
        finance.setLoanRemark(financeRequestDto.getLoanRemark());
        finance.setLoanApprovedDate(financeRequestDto.getLoanApprovedDate());
        finance.setRepaymentStartDate(financeRequestDto.getRepaymentStartDate());
        finance.setPointOfContact(financeRequestDto.getPointOfContact());
        finance.setAccountOwnerRelationship(financeRequestDto.getAccountOwnerRelationship());
    }
}
