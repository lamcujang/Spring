package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PosOrderListQueryRequest extends BaseQueryRequest  implements Serializable {

        private String documentNo;
        private String customerName;
        private Integer orgId;
        private String orderDate;
        private String fromDate;
        private String dateFrom;
        private String dateTo;
        private String toDate;
        private String orderStatus;
        private String customerKeyword;
        private Integer priceListId;
        private Integer shiftControlId;
        private String isMultiSource;
        private Integer customerId;



}
