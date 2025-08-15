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
public class RevenueByEmpDto {

    private Integer orgId;
    private String orgName;
    private String empSaleName;
    private String empSaleId;
    private BigDecimal totalAmount;
    private BigDecimal amountPercent;
}
