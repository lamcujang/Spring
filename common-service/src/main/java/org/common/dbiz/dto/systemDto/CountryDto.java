package org.common.dbiz.dto.systemDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.systemservice.domain.Country}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CountryDto implements Serializable {
    String isActive;
    Integer id;
    @NotNull
    @Size(max = 64)
    String isoCountryCode;
    @NotNull
    @Size(max = 255)
    String name;
    String description;
    @Size(max = 64)
    String currency;
    Integer tenantId;
    Integer orgId;
}