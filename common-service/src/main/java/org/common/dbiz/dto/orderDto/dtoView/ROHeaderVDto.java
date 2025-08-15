package org.common.dbiz.dto.orderDto.dtoView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;
import org.common.dbiz.dto.orderDto.ReturnReasonDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosReceiptOtherDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.productDto.PricelistDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ROHeaderVDto implements Serializable {
    Integer id;
    Integer tenantId;
    String orderStatus;
    BigDecimal totalAmount;
    BigDecimal returnFee;
    String orderDate;
    BigDecimal receiptOtherAmount;
    BigDecimal flatAmt;
    BigDecimal flatDiscount;
    BigDecimal netAmount;
    BigDecimal netReturnAmount;
    BigDecimal taxAmount;
    Integer posOrderId;
    Integer purchaseOrderId;
    ReturnReasonDto returnReason;
    String documentNo;
    String referenceNo;
    Integer doctypeId;
    String docBaseType;
    String docTypeName;
    String description;
    OrgDto org;
    UserDto user;
    VendorPOVDto vendor;
    CustomerVDto customer;
    PricelistDto priceList;
    PaymentDto payment;
    List<PosReceiptOtherDto> receiptOthers;
    List<RODetailVDto> lines;
}

