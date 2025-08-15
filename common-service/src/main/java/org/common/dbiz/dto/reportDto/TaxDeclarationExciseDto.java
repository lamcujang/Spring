package org.common.dbiz.dto.reportDto;


import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDeclarationExciseDto implements Serializable {
    Integer id;
    Integer inventoryCategorySpecialTaxId;
    String unit;
    BigDecimal exciseRevenue;
    BigDecimal exciseTaxAmount;
    String description;
    Integer taxDeclarationIndividualId;
    String isActive;
    InventoryCategorySpecialTaxDto inventoryCategorySpecialTaxDto;
}