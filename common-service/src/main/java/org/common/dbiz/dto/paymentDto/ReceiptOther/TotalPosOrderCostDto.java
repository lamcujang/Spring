package org.common.dbiz.dto.paymentDto.ReceiptOther;


import lombok.*;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.CustomerDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TotalPosOrderCostDto implements Serializable {

    Integer posOrderId;
    String documentNo;
    String orderDate;
    BigDecimal totalLine;
    BigDecimal totalAmount;
    BigDecimal totalTaxAmount;
    BigDecimal flatAmount;
    BigDecimal receiptOtherAmount;
    BigDecimal flatDiscount;
    BigDecimal totalQty;
    String billNo;
    Integer orderGuests;
    String priceCateCode;
    CustomerDto customer;
    OrgDto org;
    PosTerminalDto posTerminal;
    FloorDto floor;
    TableDto table;
    List<PosTaxLineDto> tax;
    List<PosReceiptOtherDto> receiptOther;
    List<PosOrderLineRespDto> posOrderLine;
    DeductionDto deduction;
    List<PosPaymentDto> payment;
    EInvoiceInfoDto eInvoiceInfoDto;

}
