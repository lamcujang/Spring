package org.common.dbiz.dto.orderDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoLotDto {

    Integer id;
    Integer requestOrderId;
    Integer requestOrderLineId;
    Integer lotId;
    String lotCode;
    String expirationDate;
    BigDecimal qty;
    String isActive;
}
