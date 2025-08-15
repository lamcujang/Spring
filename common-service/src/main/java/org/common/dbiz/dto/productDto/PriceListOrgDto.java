package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.PriceListOrg}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceListOrgDto implements Serializable {
    Integer id;
    @NotNull
    Integer pricelistId;
    @Size(max = 1)
    String isAll;
    @NotNull(message = "Tenant id cannot be null")
    Integer orgId;


}