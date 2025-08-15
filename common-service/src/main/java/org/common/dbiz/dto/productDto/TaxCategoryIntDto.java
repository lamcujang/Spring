package org.common.dbiz.dto.productDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.TaxCategory}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaxCategoryIntDto implements Serializable {
    Integer tenantId;
    Integer orgId;
    @Size(max = 1)
    String isActive;
    Integer id;
    @NotNull
    @Size(max = 32)
    String name;
    @Size(max = 255)
    String description;
    @NotNull
    @Size(max = 1)
    String isDefault;
    Integer erpTaxCategoryId;

    TaxDto tax;
}