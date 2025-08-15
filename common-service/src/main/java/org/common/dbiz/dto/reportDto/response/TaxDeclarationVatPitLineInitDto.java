package org.common.dbiz.dto.reportDto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.BusinessSectorGroupDto;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDeclarationVatPitLineInitDto implements Serializable {
    Integer businessSectorGroupId;
    BigDecimal vatRevenue;
    BigDecimal pitRevenue;
}