package org.common.dbiz.dto.reportDto.response.businessPerformance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DistributionSupply {
    Integer expenseTypeId;
    String code;
    String name;
    BigDecimal currentAmount;
    BigDecimal periodAmount;
    BigDecimal samePeriodLastYearAmount;
}
