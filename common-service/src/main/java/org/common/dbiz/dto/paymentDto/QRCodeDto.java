package org.common.dbiz.dto.paymentDto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QRCodeDto {

    Integer orgId;
    Integer posTerminalId;
    Integer bankAccountId;
    String amount;

}
