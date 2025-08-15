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
@Data
@SuperBuilder
public class PurchaseOrderLineDto  extends BaseDto implements Serializable {
    Integer purchaseOrderId;
    Integer productId;
    Integer lotId;
    Integer uomId;
    BigDecimal qty;
    BigDecimal priceEntered;
    Integer taxId;
    BigDecimal taxAmount;
    BigDecimal netAmount;
    BigDecimal totalAmount;
    String description;
    BigDecimal discountPercent;
    BigDecimal discountAmount;
    BigDecimal priceDiscount;
    BigDecimal totalDiscountAmount;
}