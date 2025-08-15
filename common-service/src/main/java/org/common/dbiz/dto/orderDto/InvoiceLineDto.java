package org.common.dbiz.dto.orderDto;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;

import java.io.Serializable;
import java.math.BigDecimal;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class InvoiceLineDto extends BaseDto  implements Serializable {

    Integer invoiceId;
    Integer orderLineId;
    Integer posOrderLineId;
    Integer productId;
    Integer taxId;
    BigDecimal qty;
    BigDecimal priceEntered;
    BigDecimal lineNetAmt;
    BigDecimal grandTotal;
}