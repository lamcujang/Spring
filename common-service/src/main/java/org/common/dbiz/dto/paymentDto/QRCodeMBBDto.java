package org.common.dbiz.dto.paymentDto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QRCodeMBBDto implements Serializable {

    Integer orgId;
    Integer posOrderId;
    BigDecimal amount;
    String deviceToken;

    List<PaymentDto> payments;
}
