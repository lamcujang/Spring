package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderQueryRequest extends BaseQueryRequest  implements Serializable {

    private Integer id;
    private String documentNo;
    private Integer customerId;
    private String orderDate;
    private String isActive;
    private Integer orgId;
    private Integer floorId;
    private Integer tableId; // Added field

}