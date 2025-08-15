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
@XmlRootElement(name = "AdjustInv")
public class XMLAdjustInvDto {

    @XmlElement(name = "Fkey")
    private String Fkey;

    private XMLInvoiceDto AdjustInv;

    @XmlElement(name = "Type")
    private String Type;

    @XmlElement(name = "TypeofAdj")
    private String TypeofAdj;
}


