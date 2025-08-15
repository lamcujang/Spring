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
public class ReportInvInOut {

    String invObject;
    BigDecimal startQty;
    BigDecimal startAmount;
    BigDecimal inQty;
    BigDecimal inAmount;
    BigDecimal outQty;
    BigDecimal outAmount;
    BigDecimal endQty;
    BigDecimal endAmount;
}
