package org.common.dbiz.dto.productDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BusinessSectorGroupDto implements Serializable {
    Integer tenantId;
    Integer orgId;
    @Size(max = 1)
    String isActive;
    Integer id;
    @NotNull
    @Size(max = 32)
    String name;
    @Size(max = 32)
    String code;
    String description;
    String indicatorCode;
    BigDecimal vatTaxRate;
    BigDecimal pitTaxRate;
}