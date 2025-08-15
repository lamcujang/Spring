package org.common.dbiz.dto.paymentDto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PurchaseInvoiceLineDetailDto extends BaseDto implements Serializable {
    private Integer productId;
    private String productName;
    private Integer taxId;
    private Integer uomId;
    private Integer lineNo;
    private Integer qty;
    private BigDecimal priceEntered;
    private BigDecimal discountPercent;
    private BigDecimal discountAmount;
    private String discountType;
    private BigDecimal priceDiscount;
    private BigDecimal taxAmount;
    private BigDecimal netAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscountAmount;
    private String description;
}
