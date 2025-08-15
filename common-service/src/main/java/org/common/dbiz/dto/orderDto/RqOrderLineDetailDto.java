package org.common.dbiz.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RqOrderLineDetailDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer id;
    Integer productId;
    Integer requestOrderId;
    BigDecimal qty;
    String description;
    BigDecimal saleprice;
    BigDecimal totalAmount;
    Integer taxId;
}