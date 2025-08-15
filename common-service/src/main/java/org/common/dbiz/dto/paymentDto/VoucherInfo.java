package org.common.dbiz.dto.paymentDto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VoucherInfo {

    private BigDecimal balanceAmt;
    private String customerName;
    private String voucherCode;
}
