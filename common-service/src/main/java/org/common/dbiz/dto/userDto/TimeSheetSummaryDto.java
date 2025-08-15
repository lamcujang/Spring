package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSheetSummaryDto implements Serializable {
    Integer id;
    Integer orgId;
    Integer tenantId;
    Integer employeeId;
    BigDecimal standardWorkingHours;
    BigDecimal totalWorkingHours;
    BigDecimal actualWorkingHours;
    BigDecimal overtimeHours;
    BigDecimal lateEarlyHours;
    BigDecimal monthlyOvertimeHours;
    Integer annualLeaveUsed;
    BigDecimal monthlyLateEarlyHours;
}
