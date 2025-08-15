package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLInvoiceDto {

    @XmlElement(name = "InvPattern")
    private String invPattern;

    @XmlElement(name = "InvSerial")
    private String invSerial;

    @XmlElement(name = "Type")
    private String type;

    @XmlElement(name = "ComTaxCode")
    private String comTaxCode;

    @XmlElement(name = "ComName")
    private String comName;

    @XmlElement(name = "ComAddress")
    private String comAddress;

    @XmlElement(name = "ComFax")
    private String comFax;

    @XmlElement(name = "CusCode")
    private String cusCode;

    @XmlElement(name = "CusTaxCode")
    private String cusTaxCode;

    @XmlElement(name = "CusName")
    private String cusName;

    @XmlElement(name = "Buyer")
    private String buyer;

    @XmlElement(name = "IdentityCode")
    private String identityCode;

    @XmlElement(name = "PassportNumber")
    private String passportNumber;

    @XmlElement(name = "BudgetUnitCode")
    private String budgetUnitCode;

    @XmlElement(name = "CusAddress")
    private String cusAddress;

    @XmlElement(name = "CusPhone")
    private String cusPhone;

    @XmlElement(name = "CusEmail")
    private String cusEmail;

    @XmlElement(name = "CusBankName")
    private String cusBankName;

    @XmlElement(name = "CusBankNo")
    private String cusBankNo;

    @XmlElement(name = "PaymentMethod")
    private String paymentMethod;

    @XmlElement(name = "Products")
    private XMLProductsDto products;

    @XmlElement(name = "Fees")
    private BigDecimal fees;

    @XmlElement(name = "Discount")
    private BigDecimal discount;

    @XmlElement(name = "DiscountAmount")
    private BigDecimal discountAmount;

    @XmlElement(name = "VATRate")
    private Integer vatRate;

    @XmlElement(name = "VATAmount")
    private BigDecimal vatAmount;

    @XmlElement(name = "Total")
    private BigDecimal total;

    @XmlElement(name = "Amount")
    private BigDecimal amount;

    @XmlElement(name = "AmountInWords")
    private String amountInWords;

    @XmlElement(name = "ArisingDate")
    private String arisingDate;

    @XmlElement(name = "Currency")
    private String currency;

    @XmlElement(name = "Note")
    private String note;

    @XmlElement(name = "GrossValue")
    private BigDecimal grossValue;

    @XmlElement(name = "GrossValue0")
    private BigDecimal grossValue0;

    @XmlElement(name = "VATAmount0")
    private BigDecimal vatAmount0;

    @XmlElement(name = "GrossValue5")
    private BigDecimal grossValue5;

    @XmlElement(name = "VATAmount5")
    private BigDecimal vatAmount5;

    @XmlElement(name = "GrossValue8")
    private BigDecimal grossValue8;

    @XmlElement(name = "VATAmount8")
    private BigDecimal vatAmount8;

    @XmlElement(name = "GrossValue10")
    private BigDecimal grossValue10;

    @XmlElement(name = "VATAmount10")
    private BigDecimal vatAmount10;
}
