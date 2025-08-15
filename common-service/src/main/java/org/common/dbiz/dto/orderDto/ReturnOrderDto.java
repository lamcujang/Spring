package org.common.dbiz.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class ReturnOrderDto extends BaseDto  implements Serializable {
    Integer id;
    String doctype;
    Integer userId;
    Integer vendorId;
    Integer tenantId;
    Integer orgId;
    Integer customerId;
    Integer currencyId;
    Integer priceListId;
    Integer orderId;
    Integer shiftControlId;
    Integer purchaseOrderId;
    Integer posOrderId;
    Integer locatorId;
    Integer posTerminalId;
    String documentNo;
    String orderStatus;
    String orderDate;
    BigDecimal totalAmount;
    String description;
    BigDecimal netAmount;
    BigDecimal taxAmount;
    BigDecimal netReturnAmount;
    BigDecimal flatDiscount;
    BigDecimal flatAmt;
    BigDecimal receiptOtherAmount;
    BigDecimal returnFee;
    Integer returnReasonId;
    String paymentMethod;
    Integer bankAccountId;
    String paymentDate;
    BigDecimal paymentAmount;
    List<ReturnOrderLineDto> returnOrderLines;



}