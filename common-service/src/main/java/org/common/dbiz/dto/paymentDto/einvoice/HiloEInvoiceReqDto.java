package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HiloEInvoiceReqDto {
    private String key;
    private String fkey;
    private String invPattern;
    private String invSerial;
    private String type;
    private String comTaxCode;
    private String comName;
    private String comAddress;
    private String comFax;

    private String cusCode;
    private String cusTaxCode;
    private String cusName;
    private String buyer;
    private String identityCode;
    private String passportNumber;
    private String budgetUnitCode;
    private String cusAddress;
    private String cusPhone;
    private String cusEmail;
    private String cusBankName;
    private String cusBankNo;

    private String paymentMethod;
    private List<HiloEInvoiceProductReqDto> products;

    private BigDecimal fees;
    private BigDecimal discount;
    private BigDecimal discountAmount;

    private Integer vatRate;
    private BigDecimal vatAmount;
    private BigDecimal total;
    private BigDecimal amount;
    private String amountInWords;

    private String arisingDate;
    private String currency;
    private String note;

    private BigDecimal grossValue;
    private BigDecimal grossValue0;
    private BigDecimal vatAmount0;
    private BigDecimal grossValue5;
    private BigDecimal vatAmount5;
    private BigDecimal grossValue8;
    private BigDecimal vatAmount8;
    private BigDecimal grossValue10;
    private BigDecimal vatAmount10;
}
