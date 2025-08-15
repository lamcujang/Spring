package org.common.dbiz.dto.orderDto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductToppingViewKDSDto implements Serializable {
    Integer id;
    String code;
    String name;
    Integer qty;
    Integer kitchenOrderLineId;
    Integer parentId;
}
