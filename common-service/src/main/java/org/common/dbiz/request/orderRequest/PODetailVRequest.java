package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PODetailVRequest extends BaseQueryRequest  implements Serializable {

    private Integer purchaseOrderId;
    private String isActive;
    private Integer orgId;
    private Integer tenantId;
}
