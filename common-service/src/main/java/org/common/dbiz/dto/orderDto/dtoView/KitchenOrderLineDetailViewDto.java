package org.common.dbiz.dto.orderDto.dtoView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.domain.view.KitchenOrderLineView}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KitchenOrderLineDetailViewDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer kitchenOrderLineId;
    Integer kitchenOrderId;
    @Size(max = 5)
    String orderStatusValue;
    @Size(max = 255)
    String note;
    BigDecimal qty;
    BigDecimal transferQty;
    BigDecimal cancelQty;
    @Size(max = 5)
    String priority;
    @Size(max = 255)
    String description;
    Integer cancelReasonId;
    @Size(max = 64)
    String cancelReason;
    @Size(max = 64)
    String orderStatus;
    Integer posOrderLineId;
    Integer productionId;
    ProductVDto product;
    String created;
    private String productionNo;
    Integer parentId;
}