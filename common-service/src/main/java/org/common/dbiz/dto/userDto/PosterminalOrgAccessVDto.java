package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.domain.view.PosterminalOrgAccessV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PosterminalOrgAccessVDto implements Serializable {
    BigDecimal tenantId;
    BigDecimal posTerminalId;
    BigDecimal orgId;
    @Size(max = 1)
    String isActive;
    @Size(max = 128)
    String name;
}