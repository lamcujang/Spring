package org.common.dbiz.dto.reportDto.response.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PartnerDebitDto {
    Integer id;
    String code;
    String name;
    String address;
    String taxCode;
    BigDecimal openingBalance;
    BigDecimal closingBalance;
    BigDecimal receivableAmount;
    BigDecimal amountCollected;
}
