package org.common.dbiz.dto.reportDto;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDeclarationResourceEnvironmentDto implements Serializable {
    Integer id;
    Integer taxDeclarationIndividualId;
    String resourceType;
    Integer environmentFeeId;
    BigDecimal quantity;
    BigDecimal taxBaseAmount;
    BigDecimal taxAmount;
    String description;
    String isActive;
    EnvironmentFeeDto environmentFeeDto;
}