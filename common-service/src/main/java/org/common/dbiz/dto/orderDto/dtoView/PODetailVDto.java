package org.common.dbiz.dto.orderDto.dtoView;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;
import org.common.dbiz.dto.inventoryDto.LotDto;
import org.common.dbiz.dto.orderDto.PosLotDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.dto.productDto.UomDto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PODetailVDto extends BaseDto implements Serializable {
    Integer purchaseOrderId;
    BigDecimal qty;
    BigDecimal priceEntered;
    BigDecimal netAmount;
    BigDecimal totalAmount;
    BigDecimal taxAmount;
    String description;
    BigDecimal totalDiscountAmount;
    BigDecimal discountAmount;
    BigDecimal discountPercent;
    BigDecimal priceDiscount;
    BigDecimal remainQty;
    ProductPOVDto productDto;
    TaxDto tax;
    UomDto uom;
    PosLotDto lot;
}