package org.common.dbiz.dto.reportDto.response.material;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductLedger {
    private String name;
    private BigDecimal costPrice;
    private List<TransactionRecord> raw;
    private PaymentInfo start_payment;
    private PaymentInfo end_payment;
}
