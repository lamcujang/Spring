package org.common.dbiz.dto.reportDto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReportProductRevenueDto {

    String productObject;
    BigDecimal saleQty;
    BigDecimal returnQty;
    BigDecimal returnAmount;
    BigDecimal revenue;
    BigDecimal costPrice;
    BigDecimal netRevenue;
}
