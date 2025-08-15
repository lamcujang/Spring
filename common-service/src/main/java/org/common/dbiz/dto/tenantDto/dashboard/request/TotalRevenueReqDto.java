package org.common.dbiz.dto.tenantDto.dashboard.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TotalRevenueReqDto {

    String dashBoardType;
    String displayType;
    String orgOrAll;
    Integer orgId;
    Integer tenantId;
    String queryType;
    String startDate;
    String endDate;
}
