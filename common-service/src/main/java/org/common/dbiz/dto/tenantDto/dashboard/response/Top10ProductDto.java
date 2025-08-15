package org.common.dbiz.dto.tenantDto.dashboard.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class Top10ProductDto {

    Integer productId;
    String productName;
    String productCode;
    String imageUrl;
    BigDecimal totalQty;
    BigDecimal totalAmount;
    BigDecimal qtyPercent;
    BigDecimal amountPercent;

}
