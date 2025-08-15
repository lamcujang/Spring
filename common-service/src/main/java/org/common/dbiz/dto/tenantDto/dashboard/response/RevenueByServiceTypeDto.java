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
public class RevenueByServiceTypeDto {

    String serviceType;
    BigDecimal amount;
    Integer orderNo;
    BigDecimal totalAmount;
    Integer totalOrderNo;
    BigDecimal amountPercent;
}
