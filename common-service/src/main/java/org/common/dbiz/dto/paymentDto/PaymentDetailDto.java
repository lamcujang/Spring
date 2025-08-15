package org.common.dbiz.dto.paymentDto;


import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDetailDto {

    String code;
    BigDecimal amount;
}
