package org.common.dbiz.dto.paymentDto.ReceiptOther;


import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PosReceiptOtherRespDto implements Serializable {

    Integer posOrderId;
    BigDecimal totalAmount;
    List<PosReceiptOtherDto> details;

}
