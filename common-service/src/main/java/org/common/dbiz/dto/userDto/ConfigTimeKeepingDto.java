package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.ConfigTimeKeeping}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigTimeKeepingDto implements Serializable {
    Integer id;
    OrgDto orgDto;
//    Integer orgId;
    BigDecimal standardWorkHours;
    BigDecimal maxLunchBreakMinutes;
    BigDecimal maxLateMinutes;
    BigDecimal maxEarlyLeaveMinutes;
    @Size(max = 1)
    String autoMarkAbsentIfNoCheckin;
    @Size(max = 1)
    String allowCheckoutAfterWorkHours;
    @Size(max = 1)
    String requireReasonForLateCheckin;
    @Size(max = 1)
    String allowSupplementRequest;
    BigDecimal requestDeadlineDays;
    BigDecimal maxCheckinAttemptsPerDay;
    @Size(max = 1)
    String markUnpaidLeaveIfNoCheckinAndNoRequest;
    @Size(max = 1)
    String noFaultIfLeaveRequestExists;
    @Size(max = 1)
    String allowSingleCheckinForMultipleShifts;
    @Size(max = 1)
    String autoSumWorkingHoursBetweenShifts;
    BigDecimal mergeShiftsWithinMinutes;
    BigDecimal maxShiftsPerDay;
    BigDecimal minRestBetweenShiftsMinutes;
    @Size(max = 1)
    String notifyIfNoCheckin2Days;
    @Size(max = 1)
    String warnIfLate3TimesPerWeek;
    @Size(max = 1)
    String warnIfEarlyLeave3TimesPerWeek;
    String notifyAt;
    @Size(max = 1)
    String notifyByEmail;
    @Size(max = 1)
    String notifyBySms;
    @Size(max = 1)
    String notifyInApp;
//    String orgName;
//    String orgCode;

//    Integer approverId;
//    String approverName;
//    String approverCode;
    EmployeeDto approverDto;
    EmployeeDto warningRecipientDto;
//    Integer warningRecipientId;
//    String warningRecipientCode;
//    String warningRecipientName;

}