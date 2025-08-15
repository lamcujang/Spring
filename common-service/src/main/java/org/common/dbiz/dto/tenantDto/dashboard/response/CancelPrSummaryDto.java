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
public class CancelPrSummaryDto {

    Integer cancelReasonId;
    String reasonName;
    BigDecimal qty;
    BigDecimal totalQty;
    BigDecimal qtyPercent;
}
