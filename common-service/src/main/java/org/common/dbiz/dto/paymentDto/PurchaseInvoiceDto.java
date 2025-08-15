package org.common.dbiz.dto.paymentDto;

import lombok.*;
import org.common.dbiz.dto.orderDto.PurchaseOrderDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PurchaseInvoiceDto extends BaseDto implements Serializable {
    Timestamp dateInvoicedTimestamp;
    String documentNo;
    Integer userId;
    Integer vendorId;
    String buyerName;
    String vendorTaxCode;
    String vendorAddress;
    BigDecimal totalAmount;
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
    BigDecimal discountAmount;
    BigDecimal discountPercent;
    String discountType;
    BigDecimal taxAmount;
    String issuedDate;
    String paymentDueDate;


    List<PurchaseInvoiceLineDto> purchaseOrders;
    List<PurchaseInvoiceLineDetailDto> lines;
    List<PaymentDto>  payments;
}
