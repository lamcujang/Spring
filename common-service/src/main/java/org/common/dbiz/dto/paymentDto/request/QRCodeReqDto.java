package org.common.dbiz.dto.paymentDto.request;


import lombok.*;
import org.common.dbiz.dto.paymentDto.PaymentDto;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QRCodeReqDto {

    Integer orgId;
    Integer posTerminalId;
    Integer bankAccountId;
    Integer posOrderId;
    BigDecimal amount;
    String deviceToken;

    List<PaymentDto> payments;
}
