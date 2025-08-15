package org.common.dbiz.dto.paymentDto.napas;


import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDto {

    String caseId;
    String creationDateTime;
    String id;
    String transDateTime;
    BigDecimal amount;
    String issueBank;
    String status;
    String beneficiaryBank;
    String realMerchantAccount;
    String mcc;
    String sourceAccount;
    String systemTrace;
    String localTime;
    String localDate;
    String terminalId;
    String refId;
}
