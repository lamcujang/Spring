package org.common.dbiz.dto.integrationDto.KitchenOrder;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateKitchenOrderDto {

    private Integer ad_Org_ID;
    private Integer ad_User_ID;
    private Integer d_KitchenOrderID;
    private String phone;
    private String floorNo;
    private String tableNo;
    private Integer dbiz_Pos_Order_ID;
    private Integer m_Warehouse_ID;
    private Integer terminalID;
    private String orderStatus;
    private String orderDate;
    private Integer d_PosOrderID;
    private List<KitchenOrderLineDto> lines;
}
