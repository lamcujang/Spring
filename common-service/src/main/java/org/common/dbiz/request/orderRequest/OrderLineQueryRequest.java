package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineQueryRequest  extends BaseQueryRequest  implements Serializable {

    private Integer id;
    private Integer orgId;
    private Integer orderId;
    private Integer productId;
    private BigDecimal quantity;
    private Integer taxId;
    private String isActive;
    private BigDecimal price;
    // Add other fields as necessary, following the structure of the OrderLine model
}
