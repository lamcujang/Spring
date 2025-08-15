package org.common.dbiz.dto.orderDto.response;

import lombok.*;
import org.common.dbiz.dto.orderDto.dtoView.FloorKcVDto;
import org.common.dbiz.dto.orderDto.dtoView.TableKcVDto;

import java.math.BigDecimal;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KitchenOrderGroupProductLine {

//    String tableName;
    String note;
    BigDecimal qty;
    Integer kitchenOrderLineId;
    Integer kitchenOrderId;
    TableKcVDto table;
    FloorKcVDto floor;
    List<ProductToppingViewKDSDto> topping;
}
