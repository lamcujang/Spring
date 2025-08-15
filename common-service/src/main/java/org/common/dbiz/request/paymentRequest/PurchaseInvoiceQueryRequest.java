package org.common.dbiz.request.paymentRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class PurchaseInvoiceQueryRequest extends BaseQueryRequest  implements Serializable {
    private String id;
    private String documentNo;
    private String invoiceStatus;
    private String dateFrom;
    private String dateTo;
    private String vendorKeyword;
}
