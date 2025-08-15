package org.common.dbiz.dto.orderDto.dtoView;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link }
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KitchenOrderLineDetailByStatusVDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer id;
    Integer kitchenOrderId;
    @Size(max = 255)
    String note;
    BigDecimal qty;
    @Size(max = 5)
    String statusValue;
    @Size(max = 255)
    String description;
    BigDecimal posOrderlineId;
    BigDecimal productionId;
    @Size(max = 32)
    String productionDocNo;
    @Size(max = 64)
    String cancelreason;
    @Size(max = 64)
    String orderLineStatus;
    ProductVDto product;
    private BigDecimal cookingTime;
    private BigDecimal preparationTime;
    private String priority;
    private String priorityValue;
    Integer parentId;
}