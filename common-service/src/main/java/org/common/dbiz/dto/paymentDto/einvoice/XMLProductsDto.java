package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLProductsDto {
    @XmlElement(name = "Product")
    private List<XMLProductDto> productList;
}

