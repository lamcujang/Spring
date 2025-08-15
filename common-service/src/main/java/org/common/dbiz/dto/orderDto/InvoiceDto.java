package org.common.dbiz.dto.orderDto;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;
import org.common.dbiz.dto.paymentDto.PaymentDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class InvoiceDto extends BaseDto  implements Serializable {

    Integer posOrderId;
    Integer orderId;
    Integer userId;
    Integer customerId;
    Integer vendorId;
    Integer currencyId;
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
    Integer posTerminalId;
    String orderNo;

    List<InvoiceLineDto> invoiceLines;
    List<PaymentDto>  payments;
}