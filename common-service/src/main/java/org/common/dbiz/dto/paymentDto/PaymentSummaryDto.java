package org.common.dbiz.dto.paymentDto;


import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentSummaryDto {

    BigDecimal startAmount;
    BigDecimal endAmount;
    BigDecimal totalArAmount;
    BigDecimal totalApAmount;
}
