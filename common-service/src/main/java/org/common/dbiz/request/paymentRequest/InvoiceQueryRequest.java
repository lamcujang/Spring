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

public class InvoiceQueryRequest  extends BaseQueryRequest  implements Serializable {

    private Integer id;
    private Integer tenantId;
    private Integer orgId;
    private String isActive;
    private Integer customerId;
    private Integer vendorId;
    private Integer orderId;
    private String documentNo;
    private String dateInvoiced;
    private Integer currencyId;
    private String accountingDate;
    private String buyerName;
    private String buyerTaxCode;
    private String buyerEmail;
    private String buyerAddress;
    private String buyerPhone;
    private BigDecimal totalAmount;
    private String invoiceStatus;
    private Integer priceListId;
    private Integer userId;
    private Integer referenceInvoiceId;
    private String invoiceForm;
    private String invoiceSign;
    private String invoiceNo;
    private String searchCode;
    private String searchLink;
    private String invoiceError;
    private String posOrderNo;
    private String customerName;
    private String fromDate;
    private String toDate;
    private String customerKeyword;
}
