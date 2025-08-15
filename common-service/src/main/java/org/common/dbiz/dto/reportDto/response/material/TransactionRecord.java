package org.common.dbiz.dto.reportDto.response.material;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRecord {
    private Integer stockQty;
    private Integer stockAmount;
    private Integer qtyTransaction;
    private String transactionDate;
    private Integer priceTransaction;
    private String description;
    private String documentNo;
    private String transactiontype;
}