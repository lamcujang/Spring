package org.common.dbiz.dto.orderDto.dtoView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;
import org.common.dbiz.dto.orderDto.RoLotDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosReceiptOtherDto;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.dto.productDto.UomDto;

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
public class RODetailVDto extends BaseDto implements Serializable {
    Integer id;
    Integer returnOrderId;
    Integer posOrderLineId;
    Integer purchaseOrderLineId;
    BigDecimal qty;
    BigDecimal remainQty;
    BigDecimal priceEntered;
    BigDecimal netAmount;
    BigDecimal netReturnAmount;
    BigDecimal totalAmount;
    BigDecimal taxAmount;
    String description;
    BigDecimal totalDiscountAmount;
    BigDecimal discountAmount;
    BigDecimal discountPercent;
    BigDecimal priceDiscount;
    BigDecimal returnPrice;
    BigDecimal salePrice;
    ProductPOVDto productDto;
    TaxDto tax;
    UomDto uom;
    ImageDto imageDto;
    List<RoLotDto> lots;
}