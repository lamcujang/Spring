package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetKolSameProductVRequest extends BaseQueryRequest  implements Serializable {

    private Integer kitchenOrderId;
    private Integer id;
    private String orderLineStatus;
    private Integer productId;
    private String isActive;
    private Integer orgId;
    private Integer tenantId;
    private String dateFrom;
    private String dateTo;
    private String currentDate;
    private Integer equalProductId;
    private Integer floorId;
    Integer tableId;
    private Integer warehouseId;
    String role;


}
