package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KitchenOrderRequest extends BaseQueryRequest  implements Serializable {

    private Integer warehouseId;
    private Integer floorId;
    private Integer tableId;
    private String[] orderStatus;
    private String documentno;
    private String isActive;
    private Integer orgId;
    private Integer[] ids;
    private Integer posOrderId;
    private String role;
    private String documentNo;
    private String fromDate;
    private String toDate;


}
