package org.common.dbiz.dto.reportDto;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxPaymentMethodDto implements Serializable {
    Integer id;
    String code;
    String name;
    String description;
    String isActive;
}