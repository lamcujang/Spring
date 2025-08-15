package org.common.dbiz.dto.orderDto;


import lombok.*;
import org.common.dbiz.dto.orderDto.response.PosOrderLineResDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosOrderLineRespDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosPaymentDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosTaxLineDto;
import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.UserDto;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PosOrderRetailDto {

    Integer id;
    String documentNo;
    String orderDate;
    String orderStatus;
    String orderStatusName;
    String description;
    Integer shiftControlId;
    BigDecimal totalQty;
    String billNo;
    BigDecimal totalLine;
    BigDecimal totalAmount;
    BigDecimal taxAmount;
    BigDecimal flatAmount;
    BigDecimal receiptOtherAmount;
    BigDecimal flatDiscount;
    String buyerName;
    String buyerTaxCode;
    String buyerEmail;
    String buyerAddress;
    String buyerCitizenId;
    String buyerPassportNumber;
    String buyerBudgetUnitCode;
    String buyerPhone;
    String buyerCompany;
    String isIssueEInvoice;
    String source;
    CustomerDto customer;
    OrgDto org;
    UserDto user;
    PosTerminalDto posTerminal;
    PricelistDto priceList;
    List<PosTaxLineDto> tax;
    List<PosReceiptOtherDto> receiptOther;
    List<PosOrderLineResDto> posOrderLine;
    List<PosPaymentDto> payment;
}
