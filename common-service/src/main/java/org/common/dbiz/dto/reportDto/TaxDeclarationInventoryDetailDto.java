package org.common.dbiz.dto.reportDto;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDeclarationInventoryDetailDto implements Serializable {
    private Integer id;
    private Integer taxDeclarationIndividualId;
    private String itemName;
    private String unitName;
    private BigDecimal beginQuantity;
    private BigDecimal beginAmount;
    private BigDecimal importQuantity;
    private BigDecimal importAmount;
    private BigDecimal exportQuantity;
    private BigDecimal exportAmount;
    private BigDecimal remainQuantity;
    private BigDecimal remainAmount;
    private BigDecimal beginUnitPrice;
    private BigDecimal importUnitPrice;
    private BigDecimal exportUnitPrice;
    private BigDecimal remainUnitPrice;
    private String isActive;
}