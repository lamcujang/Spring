package org.common.dbiz.dto.integrationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;

import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.PosTerminalOrgAccess}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PosTerminalOrgAccessIntDto implements Serializable {
    String isActive;
    Integer tenantId;
    Integer userId;
    Integer orgId;
    Integer posTerminalId;
    PosTerminalDto infor;
    String intUser;
}