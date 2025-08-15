package org.common.dbiz.dto.orderDto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;
import org.common.dbiz.dto.paymentDto.PaymentDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderDto extends BaseDto  implements Serializable {

    Integer posOrderId;
    List<PaymentDto>  payments;
}