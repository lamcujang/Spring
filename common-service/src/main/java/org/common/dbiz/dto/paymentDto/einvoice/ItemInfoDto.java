package org.common.dbiz.dto.paymentDto.einvoice;


import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ItemInfoDto {

    BigDecimal unitPrice;
    String itemName;
    BigDecimal quantity;
    String unitName;
    String itemCode;
    String unitCode;
    BigDecimal taxPercentage;
    BigDecimal itemTotalAmountWithoutTax;
    Integer lineNumber;
    BigDecimal taxAmount;
    String itemNote;
}
