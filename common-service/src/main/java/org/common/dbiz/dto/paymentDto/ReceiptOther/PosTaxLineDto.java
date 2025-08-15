package org.common.dbiz.dto.paymentDto.ReceiptOther;


import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PosTaxLineDto implements Serializable {

    Integer id;
    String name;
    Integer taxId;
    BigDecimal taxRate;
    BigDecimal taxAmount;
    BigDecimal taxBaseAmount;

}
