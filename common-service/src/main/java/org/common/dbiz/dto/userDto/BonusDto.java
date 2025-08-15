package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.Bonus}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class BonusDto implements Serializable {
    Integer id;
    @NotNull
    Integer orgId;
    @NotNull
    Integer tenantId;
    @NotNull
    @Size(max = 255)
    String name;
    BigDecimal price;
    @Size(max = 50)
    String bonusGroup;
    @Size(max = 50)
    String kpiBonus;
    @Size(max = 255)
    String description;
    @Size(max = 1)
    String performanceBonus;
    @Size(max = 1)
    String holidayBonus;
}