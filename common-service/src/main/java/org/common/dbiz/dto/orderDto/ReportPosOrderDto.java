package org.common.dbiz.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportPosOrderDto {
    String date;
    String documentNo;
    String qtyGuest;
    BigDecimal totalAmount;
    BigDecimal cashAmount;
    BigDecimal bankAmount;
    BigDecimal couponAmount;
    BigDecimal voucherAmount;
    BigDecimal debtAmount;
    BigDecimal freeAmount;
    BigDecimal qrCodeAmount;
    BigDecimal loyaltyAmount;
    BigDecimal visaAmount;
    String cashier;
    String shift;
    Integer totalQty;
    Integer shiftControlId;
}
