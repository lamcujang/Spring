package org.common.dbiz.dto.integrationDto.KitchenOrder;

import lombok.*;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class KitchenOrderLineDto {

    private Integer m_Product_ID;
    private BigDecimal qty;
    private String note;
    private String orderLineStatus;
    private Integer parentId;
}
