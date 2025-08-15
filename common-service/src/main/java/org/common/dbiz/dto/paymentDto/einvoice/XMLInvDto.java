package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

import javax.xml.bind.annotation.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLInvDto {
    @XmlElement(name = "key")
    private String key;

    @XmlElement(name = "Invoice")
    private XMLInvoiceDto invoice;
}


