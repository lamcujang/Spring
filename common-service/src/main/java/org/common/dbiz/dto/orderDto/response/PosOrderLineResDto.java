package org.common.dbiz.dto.orderDto.response;

import lombok.*;
import org.common.dbiz.dto.orderDto.PosLotDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.TaxDto;

import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PosOrderLineResDto {

    Integer id;
    BigDecimal qty;
    BigDecimal salePrice;
    BigDecimal priceDiscount;
    BigDecimal lineNetAmount;
    BigDecimal grandTotal;
    BigDecimal discountAmount;
    BigDecimal discountPercent;
    BigDecimal taxAmount;
    String description;
    BigDecimal remainQty;
    Integer wdsLineId;
    String wdsLineStatus;
    String wdsLineStatusName;
    ProductDto product;
    TaxDto tax;
    List<PosLotDto> lots;
}
