package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionQueryRequest extends BaseQueryRequest implements Serializable {

    private String documentno;
    private String fromDate;
    private String toDate;
    private Integer orgId;
    Integer warehouseId;
    String orderStatus;

}