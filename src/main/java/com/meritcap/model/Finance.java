package com.meritcap.model;

import com.meritcap.enums.ApprovalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "finances")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Finance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "student_budget", nullable = false)
    Double studentBudget;

    @Column(name = "loan_support_required", nullable = false)
    Boolean loanSupportRequired;

    @Column(name = "loan_amount")
    Double loanAmount;

    @Column(name = "loan_bank")
    String loanBank;

    @Column(name = "bank_ifsc_code")
    String bankIfscCode;

    @Column(name = "bank_branch")
    String bankBranch;

    @Column(name = "loan_term")
    String loanTerm;

    @Column(name = "interest_rate")
    Double interestRate;

    @Column(name = "monthly_instalment")
    Double monthlyInstalment;

    @Column(name = "collateral_required")
    Boolean collateralRequired;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_status")
    ApprovalStatus approvalStatus;

    @Column(name = "loan_remark")
    String loanRemark;

    @Column(name = "loan_approved_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate loanApprovedDate;

    @Column(name = "repayment_start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate repaymentStartDate;

    @Column(name = "point_of_contact")
    String pointOfContact;

    @Column(name = "account_owner_relationship")
    String accountOwnerRelationship;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @JoinColumn
    @OneToOne
    Student student;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
