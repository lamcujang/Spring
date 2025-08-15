package org.common.dbiz.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerQueryRequest extends BaseQueryRequest {

    private String keyword;
//    private String code;
//    private String name;
//    private String phone1;

//    private String taxCode;
//    private String email;
    private String area;
    private Integer partnerGroupId;
    private BigDecimal debitAmountFrom;
    private BigDecimal debitAmountTo;
    private String isActive;
    private BigDecimal transactionAmountFrom;
    private BigDecimal transactionAmountTo;
    private String invoiceCode;
    private String code;
}
