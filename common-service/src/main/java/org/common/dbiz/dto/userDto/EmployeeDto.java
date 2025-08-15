package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.userDto.nested.DepartmentDto;
import org.common.dbiz.dto.userDto.nested.EmployeeGradeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.Employee}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonFilter("employeeDto")
public class EmployeeDto implements Serializable {
    Integer id;
//    private Integer orgId;
    @Size(max = 1)
    String isActive;
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String code;
    @Size(max = 20)
    String gender;
    String birthday;
    @Size(max = 500)
    String placeOfBirth;
    @Size(max = 500)
    String ethnicity;
    @Size(max = 500)
    String nationality;
    @Size(max = 30)
    String identificationNumber;
    String dateOfIssue;
    @Size(max = 30)
    String placeOfIssue;
    @Size(max = 500)
    String permanentAddress;
    String employeeType;
    String joinDate;
    @Size(max = 50)
    String workStatus;
    @Size(max = 50)
    String phone;
    @Size(max = 250)
    String personalEmail;
    @Size(max = 250)
    String corporateEmail;
    @Size(max = 250)
    String contactAddress;
    @Size(max = 250)
    String contractNumber;
    @Size(max = 250)
    String contractType;
    String contractStartDate;
    String contractEndDate;
    BigDecimal baseSalary;
    BigDecimal probationSalary;
    String contractSignedDate;
    String contractTerminationDate;
    BigDecimal insuranceSalary;
    String originalContractNumber;
    @Size(max = 1)
    String hasSocialInsurance;
    String socialInsuranceNumber;
    @Size(max = 1)
    String hasHealthInsurance;
    String healthInsuranceNumber;
    @Size(max = 1)
    String hasAccidentInsurance;
    String accidentInsuranceNumber;
    BigDecimal mealAllowance;
    BigDecimal nightShiftAllowance;
    BigDecimal transportAllowance;
    BigDecimal clothingAllowance;
    BigDecimal attendanceBonus;
    BigDecimal referralBonus;
    BigDecimal holidayBonus;
    BigDecimal phoneAllowance;
    private Integer imageId;
//    Integer departmentId;
//    Integer employeeGradeId;

//    String departmentName;
//    String employeeGradeName;
//    String orgName;
    DepartmentDto departmentDto;
    OrgDto orgDto;
    EmployeeGradeDto employeeGradeDto;
    String workStatusName;
    String employeeTypeName;
    String contractTypeName;
    List<EmployeeBankDto> employeeBankList;
    List<EmployeeContactDto> employeeContactList;
}