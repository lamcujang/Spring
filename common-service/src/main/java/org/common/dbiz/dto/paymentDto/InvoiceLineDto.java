package org.common.dbiz.dto.paymentDto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceLineDto extends BaseDto implements Serializable {

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