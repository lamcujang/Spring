package org.common.dbiz.request.orderRequest;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SendNotifycationRq implements Serializable {
    private Integer posOrderId;
    private Integer kitchenOrderLineId;
    private String priorityLevel;
    private String note;
    private String preparationTime;
    private String cookingTime;
    private String notifyType;
    private Integer productId;

    Integer kitchenOrderId;
    String tableName;
    String floorName;

    Integer tableId;
    Integer floorId;
    Integer orgId;
    Integer posTerminalId;
}
