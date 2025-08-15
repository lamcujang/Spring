package org.common.dbiz.dto.reportDto;

import lombok.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndividualIndustryDto implements Serializable {
    Integer id;
    Integer taxDeclarationIndividualId;
    Integer taxBusinessIndustryId;
    String isActive;
    TaxBusinessIndustryDto taxBusinessIndustryDto;
}