package org.common.dbiz.dto.paymentDto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 */


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceDto extends BaseDto implements Serializable {

    Integer posOrderId;
    Integer orderId;
    Integer userId;
    Integer customerId;
    Integer vendorId;
    Integer currencyId;
    String cumstomerCode;
    String buyerName;
    String buyerTaxCode;
    String buyerEmail;
    String buyerAddress;
    String buyerPhone;
    String buyerCompany;
    String buyerCitizenId;
    String buyerPassportNumber;
    String buyerBudgetUnitCode;
    BigDecimal totalAmount;
    Integer priceListId;
    Integer referenceInvoiceId;
    String invoiceForm;
    String invoiceSign;
    String invoiceNo;
    String searchCode;
    String searchLink;
    String invoiceError;
    String dateInvoiced;
    String accountingDate;
    String description;
    Integer posTerminalId;
    String orderNo;
    String documentno;
    String issuedDate;
    Timestamp dateInvoicedTimestamp;
    String dInvoiceUu;
    String priceCateCode;

    List<InvoiceLineDto> invoiceLines;
    List<PaymentDto>  payments;
}