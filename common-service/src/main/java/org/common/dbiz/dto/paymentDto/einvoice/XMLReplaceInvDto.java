package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ReplaceInv")
public class XMLReplaceInvDto {

    @XmlElement(name = "key")
    private String key;

    @XmlElement(name = "Fkey")
    private String Fkey;

    private XMLInvoiceDto ReplaceInv;
}


