package org.common.dbiz.dto.paymentDto;

import lombok.*;
import org.common.dbiz.dto.orderDto.dtoView.ProductPOVDto;
import org.common.dbiz.dto.orderDto.response.ProductDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.dto.productDto.UomDto;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PurchaseInvoiceLineDetailVDto extends BaseDto implements Serializable {
    private Integer purchaseInvoiceId;
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
    private Integer productId;
    private String productName;
    private String productCode;
    private Integer taxId;
    private String taxName;
    private BigDecimal taxRate;
    private Integer uomId;
    private String uomName;
//    private ProductPOVDto product;
//    private TaxDto tax;
//    private UomDto uom;
}
