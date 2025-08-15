package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HiloEInvoiceProductReqDto {
    private Integer orderBy;
    private String code;
    private String prodName;
    private BigDecimal prodPrice;
    private BigDecimal prodQuantity;
    private String prodUnit;
    private BigDecimal total;
    private Integer vatRate;
    private BigDecimal vatAmount;
    private Boolean isSum;
    private BigDecimal discount;
    private BigDecimal discountAmount;
    private BigDecimal amount;
    private Integer characteristic;
    private String extra01;
    private String extra02;
}

