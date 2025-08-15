package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.Locator}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocatorDto implements Serializable {
    @NotNull
    @Size(max = 1)
    String isActive;
    Integer id;
    @Size(max = 32)
    String code;
    @Size(max = 255)
    String description;
    @Size(max = 32)
    String x;
    @Size(max = 32)
    String y;
    @Size(max = 32)
    String z;
    private Integer warehouseId;

    String name;
    Integer orgId;
    String isDefault;
}