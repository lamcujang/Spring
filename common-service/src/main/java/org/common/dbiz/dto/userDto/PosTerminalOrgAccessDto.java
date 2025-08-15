package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.PosTerminalOrgAccess}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PosTerminalOrgAccessDto implements Serializable {
    String isActive;
    Integer tenantId;
    Integer userId;
    Integer orgId;
    Integer posTerminalId;
}