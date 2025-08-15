package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.SalaryConfig}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalaryConfigDto implements Serializable {
    Integer id;
    Integer orgId;
    BigDecimal otLimitPerDay;
    @Size(max = 1)
    String requireOtApprovalBeforeWork;
    BigDecimal otCoefficientWeekday;
    BigDecimal otCoefficientWeekend;
    @Size(max = 1)
    String autoOtWeekend;
    @Size(max = 1)
    String autoOtHoliday;
    BigDecimal otCoefficientHoliday;
    BigDecimal applyFrom;
    BigDecimal applyTo;
    BigDecimal otCoefficientNight;

    @Size(max = 1)
    String notifyOtRequest;
    @Size(max = 1)
    String autoRejectUnapprovedOt;
    BigDecimal totalWorkHoursLt;
    BigDecimal underworkCoefficientLt;
    BigDecimal totalWorkHoursFrom;
    BigDecimal totalWorkHoursTo;
    BigDecimal underworkCoefficientTo;
    @Size(max = 1)
    String excludeLunchBreakFromUnderwork;
    @Size(max = 1)
    String roundUnderworkTime15min;
    @Size(max = 1)
    String allowUnderworkOffsetByOt;
    BigDecimal socialInsuranceEmployeeRate;
    BigDecimal healthInsuranceEmployeeRate;
    BigDecimal accidentInsuranceEmployeeRate;
    BigDecimal socialInsuranceCompanyRate;
    BigDecimal healthInsuranceCompanyRate;
    BigDecimal accidentInsuranceCompanyRate;
    BigDecimal laborAccidentInsurance;
    BigDecimal minInsuranceContribution;
    BigDecimal maxInsuranceContribution;
    @Size(max = 1)
    String autoCalculateEndMonthSalary;
    @Size(max = 1)
    String autoSendPayslipEmail;
    @Size(max = 1)
    String autoBackupData;
    @Size(max = 1)
    String remindClosingBeforeSalaryCalc;
    @Size(max = 1)
    String alertExceedOtLimit;
    @Size(max = 1)
    String notifyAbsence;
    @Size(max = 1)
    String alertSalaryNotCalculated;
    @Size(max = 1)
    String alertAttendanceNotClosed;
    @Size(max = 25)
    String salaryCycle;
    @Size(max = 25)
    String salaryCalcDay;
    @Size(max = 25)
    String notificationEmail;

    BigDecimal autoApprovalLimit;
    @Size(max = 1)
    String notifyEmailOnReward;
    @Size(max = 1)
    String logRewardHistory;
    @Size(max = 1)
    String dailyApprovalReport;
//    Integer otApprover;
//    Integer rewardApprover;
    EmployeeDto otApproverDto;
    EmployeeDto rewardApproverDto;
    List<PenaltyDeductionDto> penaltyDeductions;
    List<BonusDto> bonuses;
    List<AllowanceDto> allowances;
}