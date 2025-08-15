package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PosOrderQueryRequest extends BaseQueryRequest  implements Serializable {

        private String documentNo;
        private Integer customerId;
        private String orderDate;
        private String isActive;
        private Integer orgId;
        private String[] orderStatus;
        private Integer floorId;
        private Integer tableId;
        private Integer posOrderId;

}
