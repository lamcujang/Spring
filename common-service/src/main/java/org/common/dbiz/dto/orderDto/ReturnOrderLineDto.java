package org.common.dbiz.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class ReturnOrderLineDto extends BaseDto implements Serializable {
    Integer returnOrderId;
    Integer purchaseOrderlineId;
    Integer posOrderlineId;
    Integer productId;
    Integer uomId;
    BigDecimal qty;
    BigDecimal priceEntered;
    Integer taxId;
    BigDecimal taxAmount;
    BigDecimal netAmount;
    BigDecimal netReturnAmount;
    BigDecimal totalAmount;
    String description;
    BigDecimal discountPercent;
    BigDecimal discountAmount;
    BigDecimal priceDiscount;
    BigDecimal totalDiscountAmount;
    BigDecimal salePrice;
    BigDecimal returnPrice;
    List<RoLotDto> lots;
}