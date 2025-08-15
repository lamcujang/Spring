package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

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
public class TaxCategoryDto implements Serializable {
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

    TaxDto taxDto;

}