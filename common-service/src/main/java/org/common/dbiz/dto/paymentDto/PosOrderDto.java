package org.common.dbiz.dto.paymentDto;

import java.io.Serializable;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PosOrderDto extends BaseDto implements Serializable {
    Integer bankId;
    Integer bankAccountId;
    String qrcodePayment;
    Integer npOrderId;
    String deviceToken;
}










