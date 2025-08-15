package org.common.dbiz.dto.paymentDto.ReceiptOther;

import lombok.*;
import org.common.dbiz.dto.productDto.ProductDto;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PosOrderLineRespDto {

    Integer id;
    Integer posOrderLineId;
    Integer productId;
    String productName;
    String productWTaxName;
    BigDecimal salePrice;
    BigDecimal qty;
    BigDecimal lineNetAmount;
    BigDecimal grandTotal;


}
