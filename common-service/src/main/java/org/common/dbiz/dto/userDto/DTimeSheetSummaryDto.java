package org.common.dbiz.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DTimeSheetSummaryDto {
    BigDecimal standardWorkingHours;
    BigDecimal totalWorkingHours;
    BigDecimal actualWorkingHours;
    BigDecimal overtimeHours;
    BigDecimal lateEarlyHours;
    BigDecimal monthlyOvertimeHours;
    Integer annualLeaveUsed;
    BigDecimal monthlyLateEarlyHours;
    EmployeeDto employee;
    List<LeaveApplicationDto> leaveApplication;
    List<OverTimeLogDto> overTimeLogDto;
//    OrgDto org;
//    DepartmentDto department;
//    EmployeeGradeDto employeeGrade;
}
