package org.common.dbiz.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HTimeSheetSummaryDto {
    BigDecimal standardWorkingHours;
    BigDecimal totalWorkingHours;
    BigDecimal actualWorkingHours;
    BigDecimal overtimeHours;
    BigDecimal lateEarlyHours;
    BigDecimal monthlyOvertimeHours;
    Integer annualLeaveUsed;
    BigDecimal monthlyLateEarlyHours;
    EmployeeDto employee;
    OrgDto org;
    DepartmentDto department;
    EmployeeGradeDto employeeGrade;
}
