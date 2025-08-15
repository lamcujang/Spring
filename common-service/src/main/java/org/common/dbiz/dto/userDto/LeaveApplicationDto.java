package org.common.dbiz.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LeaveApplicationDto {
    private Integer id;
    private Integer orgId;
    private Integer tenantId;
    private Integer timesheetSummaryId;
    private String leaveType;
    private String fromDate;
    private String toDate;
    private Integer approverId;
    private String reason;
}
