package org.common.dbiz.dto.paymentDto.napas;


import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReconciliationReportDTDto {

    String id;
    String transDateTime;
    BigDecimal amount;
    String issueBank;
    String status;
    String beneficiaryBank;
    String realMerchantAccount;
    String mcc;
    String refId;
    String creditTrace;
    String creditReferenceNumber;
    String reserveInfo;
}
