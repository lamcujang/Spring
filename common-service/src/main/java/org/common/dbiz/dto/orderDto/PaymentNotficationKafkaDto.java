package org.common.dbiz.dto.orderDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class PaymentNotficationKafkaDto {

    String code;
    BigDecimal  amount;
    String deviceToken;
    Integer tenantId;
}
