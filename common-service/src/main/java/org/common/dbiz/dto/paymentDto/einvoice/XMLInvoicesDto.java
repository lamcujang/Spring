package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@XmlRootElement(name = "Invoices")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLInvoicesDto {
    @XmlElement(name = "Inv")
    private List<XMLInvDto> invs;
}
