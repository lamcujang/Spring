package org.common.dbiz.dto.reportDto.material;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductLedger {
    private String name;
    private List<TransactionRecord> raw;
    private PaymentInfo start_payment;
    private PaymentInfo end_payment;
}
