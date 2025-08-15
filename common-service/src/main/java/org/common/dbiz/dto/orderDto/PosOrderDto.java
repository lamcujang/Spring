package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosTaxLineDto;
import org.common.dbiz.dto.productDto.ImageDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class PosOrderDto implements Serializable {

    Integer orgId;
    String isActive;
    Integer id;
    Integer customerId;
    @Size(max = 15)
    String phone;
    @Size(max = 5)
    String orderStatus;
    @Size(max = 10)
    String source;
    @Size(max = 1)
    String isLocked;
    Integer tableId;
    Integer floorId;
    Integer userId;
    Integer orderGuests;
    String isProcessed;
    String isSyncErp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String orderDate;
    @Size(max = 255)
    String customerName;
    String documentNo;
    Integer currencyId;
    Integer pricelistId;
    Integer posTerminalId;
    Integer bankId;
    Integer bankAccountId;
    String qrcodePayment;
    Integer doctypeId;
    Integer shiftControlId;
    Integer warehouseId;
    String description;
    Integer cancelReasonId;
    String cancelReasonMessage;
    Integer cancelUserId;
    Integer erpPosOrderId;
    String erpPosOrderNo;
    String billNo;
    String isSplit;
    Integer requestOrderId;
    ImageDto imageTransaction;
    Integer kitchenOrderId;
    String priceCateCode;

    BigDecimal totalAmount;
    BigDecimal totalLine;
    BigDecimal flatAmt;
    BigDecimal flatDiscount;
    BigDecimal taxAmount;
    BigDecimal receiptOtherAmount;

    BigDecimal promoPercent;
    BigDecimal promoAmount;

    String buyerName;
    String buyerTaxCode;
    String buyerEmail;
    String buyerAddress;
    String buyerPhone;
    String buyerCompany;
    String buyerCitizenId;
    String buyerPassportNumber;
    String buyerBudgetUnitCode;
    String isIssueEInvoice;
    List<PosOrderLineVAllDto> posOrderLines;
    List<PosTaxLineDto> tax;
    List<PosReceiptOtherDto> receiptOther;
    List<PaymentDto>  payments;
}