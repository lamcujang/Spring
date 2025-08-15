package org.common.dbiz.dto.reportDto.response.revenue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RevenueChildDTO {
    private String code;
    private String name;
    private BigDecimal totalAmount;
}
