package org.common.dbiz.dto.paymentDto.napas;


import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvestigationDto {

    String caseId;
    String id;
    String transDateTime;
    BigDecimal amount;

}
