package org.common.dbiz.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OverTimeLogDto {
    private Integer id;
    private Integer orgId;
    private Integer tenantId;
    private Integer timesheetSummaryId;
    private String overtimeDate;
    private BigDecimal overtimeHours;
    private String overtimeType;
}
