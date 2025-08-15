package org.common.dbiz.dto.orderDto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class PaymentDto implements Serializable {

    String paymentRule;
    BigDecimal paymentAmount;
    String code;

}
