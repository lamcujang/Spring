package org.common.dbiz.dto.paymentDto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.common.dbiz.dto.paymentDto.ReceiptOther.EInvoiceInfoDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDto extends BaseDto  implements Serializable {

    Integer customerId;
    Integer vendorId;
    Integer invoiceId;
    Integer purchaseInvoiceId;
    Integer posOrderId;
    Integer bankAccountId;
    String paymentStatus;
    BigDecimal paymentAmount;
    String paymentDate;
    Integer orderId;
    String paymentRule;
    String paymentMethod;
    String code;
    String userGroup;
    Integer responsibleUserId;
    String docType;
    String referenceNo;
    String description;
    Integer userId;
    Integer posTerminalId;
    String transactionId;// neu co visa truyen
    String documentNo;
    Integer returnOrderId;
    Integer purchaseOrderId;
    String isOriginal;
    String qrCode;
    Integer expenseTypeId;
    String expenseTypeName;
    @JsonSerialize(using = ToStringSerializer.class)
    BigDecimal remainAmount;
    List<PaymentDetailDto> details;
    EInvoiceInfoDto eInvoice;
}