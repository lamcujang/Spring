package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "d_employee", schema = "pos")
public class Employee extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_employee_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_employee_sq")
    @SequenceGenerator(name = "d_employee_sq", sequenceName = "d_employee_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;


    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Size(max = 20)
    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Size(max = 500)
    @Column(name = "place_of_birth", length = 500)
    private String placeOfBirth;

    @Size(max = 500)
    @Column(name = "ethnicity", length = 500)
    private String ethnicity;

    @Size(max = 500)
    @Column(name = "nationality", length = 500)
    private String nationality;

    @Size(max = 30)
    @Column(name = "identification_number", length = 30)
    private String identificationNumber;

    @Column(name = "date_of_issue")
    private LocalDate dateOfIssue;

    @Size(max = 30)
    @Column(name = "place_of_issue", length = 30)
    private String placeOfIssue;

    @Size(max = 500)
    @Column(name = "permanent_address", length = 500)
    private String permanentAddress;

    @Column(name = "employee_type")
    @Type(type = "org.hibernate.type.TextType")
    private String employeeType;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Size(max = 50)
    @Column(name = "work_status", length = 50)
    private String workStatus;

    @Size(max = 50)
    @Column(name = "phone", length = 50)
    private String phone;

    @Size(max = 250)
    @Column(name = "personal_email", length = 250)
    private String personalEmail;

    @Size(max = 250)
    @Column(name = "corporate_email", length = 250)
    private String corporateEmail;

    @Size(max = 250)
    @Column(name = "contact_address", length = 250)
    private String contactAddress;

    @Size(max = 250)
    @Column(name = "contract_number", length = 250)
    private String contractNumber;

    @Size(max = 250)
    @Column(name = "contract_type", length = 250)
    private String contractType;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    @Column(name = "base_salary")
    private BigDecimal baseSalary;

    @Column(name = "probation_salary")
    private BigDecimal probationSalary;

    @Column(name = "contract_signed_date")
    private LocalDate contractSignedDate;

    @Column(name = "contract_termination_date")
    private LocalDate contractTerminationDate;

    @Column(name = "insurance_salary")
    private BigDecimal insuranceSalary;

    @Column(name = "original_contract_number")
    @Type(type = "org.hibernate.type.TextType")
    private String originalContractNumber;

    @Size(max = 1)
    @Column(name = "has_social_insurance", length = 1)
    private String hasSocialInsurance;

    @Column(name = "social_insurance_number")
    @Type(type = "org.hibernate.type.TextType")
    private String socialInsuranceNumber;

    @Size(max = 1)
    @Column(name = "has_health_insurance", length = 1)
    private String hasHealthInsurance;

    @Column(name = "health_insurance_number")
    @Type(type = "org.hibernate.type.TextType")
    private String healthInsuranceNumber;

    @Size(max = 1)
    @Column(name = "has_accident_insurance", length = 1)
    private String hasAccidentInsurance;

    @Column(name = "accident_insurance_number")
    @Type(type = "org.hibernate.type.TextType")
    private String accidentInsuranceNumber;

    @Column(name = "night_shift_allowance", length = 1)
    private BigDecimal nightShiftAllowance;
//    @Size(max = 1)
//    @Column(name = "position_allowance", length = 1)
//    private String positionAllowance;

    @Column(name = "phone_allowance", length = 1)
    private BigDecimal phoneAllowance;

    @Column(name = "meal_allowance", length = 1)
    private BigDecimal mealAllowance;

//    @Size(max = 1)
//    @Column(name = "travel_allowance", length = 1)
//    private String travelAllowance;

    @Column(name = "transport_allowance", length = 1)
    private BigDecimal transportAllowance;

    @Column(name = "clothing_allowance", length = 1)
    private BigDecimal clothingAllowance;

//    @Size(max = 1)
//    @Column(name = "seniority_allowance", length = 1)
//    private String seniorityAllowance;

//    @Size(max = 1)
//    @Column(name = "other_allowance", length = 1)
//    private String otherAllowance;

//    @Size(max = 1)
//    @Column(name = "kpi_bonus", length = 1)
//    private String kpiBonus;

//    @Size(max = 1)
//    @Column(name = "sales_bonus", length = 1)
//    private String salesBonus;

    @Column(name = "attendance_bonus", length = 1)
    private BigDecimal attendanceBonus;

//    @Size(max = 1)
//    @Column(name = "thirteenth_month_bonus", length = 1)
//    private String thirteenthMonthBonus;

//    @Size(max = 1)
//    @Column(name = "tet_bonus", length = 1)
//    private String tetBonus;

//    @Size(max = 1)
//    @Column(name = "birthday_bonus", length = 1)
//    private String birthdayBonus;

//    @Size(max = 1)
//    @Column(name = "project_completion_bonus", length = 1)
//    private String projectCompletionBonus;

    @Column(name = "referral_bonus", length = 1)
    private BigDecimal referralBonus;

//    @Size(max = 1)
//    @Column(name = "innovation_bonus", length = 1)
//    private String innovationBonus;

//    @Size(max = 1)
//    @Column(name = "training_participation_bonus", length = 1)
//    private String trainingParticipationBonus;

    @Column(name = "holiday_bonus", length = 1)
    private BigDecimal holidayBonus;

    @Column(name = "image_id")
    private Integer imageId;


    @Column(name = "d_department_id")
    private Integer departmentId;

    @Column(name = "d_employee_grade_id")
    private Integer employeeGradeId;

}