package org.common.dbiz.dto.orderDto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KitchenOrderlineDetailDto implements Serializable {
    String isActive;
    Integer tenantId;
    Integer orgId;
    Integer id;
    Integer cancelReasonId;
    @NotNull
    Integer kitchenOrderId;
    @Size(max = 5)
    String orderlineStatus;
    @NotNull
    Integer productId;
    @Size(max = 255)
    String note;
    @NotNull
    BigDecimal qty;
    BigDecimal transferQty;
    BigDecimal cancelQty;
    @Size(max = 5)
    String priority;
    @Size(max = 255)
    String description;
    Integer posOrderLineId;
    Integer productionId;
    Integer parentId;

    TableDto table;
    FloorDto floor;

}