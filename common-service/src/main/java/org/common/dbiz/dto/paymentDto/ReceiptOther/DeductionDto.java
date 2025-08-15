package org.common.dbiz.dto.paymentDto.ReceiptOther;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeductionDto {

    private BigDecimal deductionPercent;
    private BigDecimal deductionAmount;
    private String deductionDescription;

}
