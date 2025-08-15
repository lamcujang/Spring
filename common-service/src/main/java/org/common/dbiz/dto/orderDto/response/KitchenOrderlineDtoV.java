package org.common.dbiz.dto.orderDto.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.dto.KitchenOrderlineDto}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KitchenOrderlineDtoV implements Serializable
{
    Integer id;
    Integer cancelReasonId;
    Integer kitchenOrderId;
    String orderlineStatus;
    ProductDto product;
    String note;
    BigDecimal qty;
    String description;
    Integer posOrderLineId;
    Integer productionId;
    Integer parentId;
}