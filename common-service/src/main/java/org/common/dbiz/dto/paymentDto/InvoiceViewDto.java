package org.common.dbiz.dto.paymentDto;

import lombok.*;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.dto.orderDto.PosOrderLineVAllDto;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.dto.orderDto.response.PosOrderLineDtoV;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosTaxLineDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.tenantDto.PosTerminalV;
import org.common.dbiz.dto.tenantDto.PosTerminalVDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceViewDto extends BaseDto implements Serializable {

    Integer orderId;
    String accountingDate;
    String dateInvoiced;
    String documentNo;
    BigDecimal totalAmount;
    String invoiceStatus;
    Integer customerId;
    String customerName;
    Integer vendorId;
    String vendorName;
    String buyerName;
    String buyerTaxCode;
    String buyerEmail;
    String buyerAddress;
    String buyerPhone;
    Integer priceListId;
    String priceListName;
    Integer userId;
    String userName;
    String fullName;
    Integer referenceInvoiceId;
    String invoiceForm;
    String invoiceSign;
    String invoiceNo;
    String searchCode;
    String searchLink;
    String invoiceError;
    BigDecimal paid;
    String posOrderNo;
    String orderStatus;
    private String customerPhone;
    private String tableName;
    private Integer orderGuests;
    private String orgName;
    private String orgAddress;

    private String einvoiceValueStatus;
    private String einvoiceStatus;
    private String einvoiceSupplierTaxCode;
    private String einvoiceTransactionId;
    private String einvoiceReservationCode;
    private String einvoiceTaxCode;
    private String issuedDate;

    BigDecimal discountAmount;
    BigDecimal receiptOtherAmount;
    BigDecimal taxAmount;
    String priceCateCode;

    PosTerminalDto posTerminal;
    TableDto table;
    FloorDto floor;

    List<PosTaxLineDto> tax;
    List<PosReceiptOtherDto> receiptOther;
    List<PosOrderLineVAllDto> lineDto;
    List<PaymentViewDto> paymentDtos;
    List<InvoiceLineViewDto> invoiceLine;
}