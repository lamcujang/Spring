package org.common.dbiz.request.orderRequest;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CancelPosOrderRequest {

    private Integer requestOrderId;

    private Integer posOrderId;

    private Integer cancelReasonId;

    private BigDecimal qty;

    private String Status;

    private Integer[] requestOrderLineId;

}
