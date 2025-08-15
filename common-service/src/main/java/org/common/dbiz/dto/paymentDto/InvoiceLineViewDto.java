package org.common.dbiz.dto.paymentDto;

import lombok.*;
import org.common.dbiz.dto.orderDto.PosOrderLineVAllDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceLineViewDto implements Serializable {
    String code;
    String name;
    String uomName;
    BigDecimal qty;
    BigDecimal priceEntered;
    BigDecimal salePrice;
    BigDecimal grandTotal;
    BigDecimal lineNetAmt;
}