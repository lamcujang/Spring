package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.domain.Tax}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaxDto implements Serializable {
    Integer tenantId;
    Integer orgId;
    @Size(max = 1)
    String isActive;
    Integer id;
    @Size(max = 64)
    String name;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    Integer taxCategoryId;
    BigDecimal taxRate;
    @Size(max = 1)
    String isDefault;
    @Size(max = 1)
    String isSaleTax;
    Integer erpTaxId;
    TaxCategoryDto taxCategoryDto;
}