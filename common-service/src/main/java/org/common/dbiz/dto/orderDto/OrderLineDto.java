package org.common.dbiz.dto.orderDto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderLineDto extends BaseDto  implements Serializable {

    Integer productId;
    Integer orderId;
    BigDecimal qty;
    BigDecimal priceEntered;
    BigDecimal lineNetAmt;
    BigDecimal grandTotal;
    Integer taxId;
    BigDecimal taxAmount;
    BigDecimal discountPercent;
    BigDecimal discountAmount;
}