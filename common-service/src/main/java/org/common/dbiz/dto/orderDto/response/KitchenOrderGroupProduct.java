package org.common.dbiz.dto.orderDto.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KitchenOrderGroupProduct {

    ProductDto productDto;

    String dateOrdered;

    String orderStatus;

    Integer productionId;

    String time;



    List<KitchenOrderGroupProductLine> kitchenOrderGroupProductLines;

}
