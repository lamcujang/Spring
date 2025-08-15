package org.common.dbiz.dto.integrationDto.posOrder;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LineInfoDto {

    private Integer m_Product_ID;
    private BigDecimal qtyOrdered;
    private BigDecimal priceActual;
    private BigDecimal priceBeforeDiscount;
    private Integer taxID;
    private String taxName;
    private BigDecimal taxAmt;
}
