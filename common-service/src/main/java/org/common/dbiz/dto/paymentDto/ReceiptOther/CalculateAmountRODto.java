package org.common.dbiz.dto.paymentDto.ReceiptOther;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CalculateAmountRODto {

    Integer orgId;
    Integer posOrderId;
    Integer posTerminalId;
    List<ChooseReceiptOtherDto> listReceiptOther;
    DeductionDto deduction;

}
