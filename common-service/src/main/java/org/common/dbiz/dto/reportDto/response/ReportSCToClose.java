package org.common.dbiz.dto.reportDto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReportSCToClose {

    BigDecimal totalAmount;
    BigDecimal totalGuests;
    BigDecimal cashAmount;
    BigDecimal bankAmount;
    BigDecimal visaAmount;
    BigDecimal debtAmount;
    BigDecimal loyaltyAmount;
    BigDecimal couponAmount;
    BigDecimal freeAmount;
    BigDecimal qrcodeAmount;
    BigDecimal voucherAmount;
    String object;
}
