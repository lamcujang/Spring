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
public class PosLotDto {

    Integer id;
    Integer posOrderId;
    Integer posOrderLineId;
    Integer lotId;
    String lotCode;
    String expirationDate;
    BigDecimal qty;
    BigDecimal salePrice;
    String isActive;

}
