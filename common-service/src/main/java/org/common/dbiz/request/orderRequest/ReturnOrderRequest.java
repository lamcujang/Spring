package org.common.dbiz.request.orderRequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnOrderRequest extends BaseQueryRequest  implements Serializable {
    private Integer id;
    private String isActive;
    private Integer orgId;
    private Integer tenantId;
    private String nameReference;
    private String dateFrom;
    private String dateTo;
    private String orderStatus;
    private String documentNo;
    private String poDocumentNo;
    private String doctypeCode;
    private String paymentMethod;
    private String userSell;
}
