package org.common.dbiz.dto.reportDto;
import lombok.*;
import org.common.dbiz.dto.productDto.BusinessSectorGroupDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDeclarationVatPitLineDto implements Serializable {
    Integer id;
    Integer taxDeclarationIndividualId;
    Integer businessSectorGroupId;
    String itemCode;
    BigDecimal vatRevenue;
    BigDecimal vatAmount;
    BigDecimal pitRevenue;
    BigDecimal pitAmount;
    String isActive;
    BusinessSectorGroupDto businessSectorGroupDto;
}