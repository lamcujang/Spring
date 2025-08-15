package org.common.dbiz.request.orderRequest;

import lombok.*;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KitchenOrderLineCancelRq implements Serializable {
    private Integer kitchenOrderLineId;
    private Integer cancelReasonId;
    private Integer cancelQty;
    private String note;
}
