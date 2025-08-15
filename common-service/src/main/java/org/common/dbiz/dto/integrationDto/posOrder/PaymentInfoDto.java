package org.common.dbiz.dto.integrationDto.posOrder;

import lombok.*;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PaymentInfoDto {

    private BigDecimal paymentAmt;
    private String paymentRule;
    private String voucherNo;
    private Integer point;
    private String transID;
}
