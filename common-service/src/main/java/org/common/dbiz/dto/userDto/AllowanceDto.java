package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.Allowance}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class AllowanceDto implements Serializable {
    Integer id;
    Integer orgId;
    Integer tenantId;
    String name;
    BigDecimal price;
}