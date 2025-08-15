package org.common.dbiz.dto.tenantDto.dashboard.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TotalRevenueRespDto {

    Integer tenantId;
    Integer orgId;
    String orgName;
    BigDecimal orgTotalRevenue;
    BigDecimal revenuePercentage;
    BigDecimal revenueByPercentage;
    String revenueObject;
}
