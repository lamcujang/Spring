package org.common.dbiz.request.orderRequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class POHeaderVRequest extends BaseQueryRequest  implements Serializable {
    private Integer id;
    private String isActive;
    private Integer orgId;
    private Integer tenantId;
    private String nameReference;
    private String searchKey;
    private String dateFrom;
    private String dateTo;
    private String orderStatus;
    private String vendorKeyword;
    private String documentNo;
    private Integer purchaseInvoiceId;
}
