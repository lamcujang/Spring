package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLProductDto {

    @XmlElement(name = "OrderBy")
    private Integer orderBy;

    @XmlElement(name = "Code")
    private String code;

    @XmlElement(name = "ProdName")
    private String prodName;

    @XmlElement(name = "ProdPrice")
    private BigDecimal prodPrice;

    @XmlElement(name = "ProdQuantity")
    private BigDecimal prodQuantity;

    @XmlElement(name = "ProdUnit")
    private String prodUnit;

    @XmlElement(name = "Total")
    private BigDecimal total;

    @XmlElement(name = "VATRate")
    private Integer vatRate;

    @XmlElement(name = "VATAmount")
    private BigDecimal vatAmount;

    @XmlElement(name = "IsSum")
    private Boolean isSum;

    @XmlElement(name = "Discount")
    private BigDecimal discount;

    @XmlElement(name = "DiscountAmount")
    private BigDecimal discountAmount;

    @XmlElement(name = "Amount")
    private BigDecimal amount;

    @XmlElement(name = "Characteristic")
    private Integer characteristic;

    @XmlElement(name = "Extra01")
    private String extra01;

    @XmlElement(name = "Extra02")
    private String extra02;
}

