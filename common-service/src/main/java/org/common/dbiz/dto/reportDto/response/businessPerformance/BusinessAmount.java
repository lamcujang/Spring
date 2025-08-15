package org.common.dbiz.dto.reportDto.response.businessPerformance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BusinessAmount {
    BigDecimal sumCurrentAmount;
    BigDecimal sumPeriodAmount;
    BigDecimal sumSamPeriodYearAmount;
    List<DistributionSupply> raws;
}
