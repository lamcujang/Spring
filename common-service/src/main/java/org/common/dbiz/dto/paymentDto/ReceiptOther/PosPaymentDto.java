package org.common.dbiz.dto.paymentDto.ReceiptOther;

import lombok.*;
import org.common.dbiz.dto.paymentDto.PaymentDetailDto;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PosPaymentDto {

    Integer id;
    String name;
    String paymentMethod;
    BigDecimal amount;
    List<PaymentDetailDto> details;


}
