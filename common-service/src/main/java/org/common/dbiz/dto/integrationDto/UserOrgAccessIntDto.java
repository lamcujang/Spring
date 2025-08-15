package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.userDto.PosTerminalOrgAccessDto;
import org.common.dbiz.dto.userDto.jsonView.JsonViewUserDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.UserOrgAccess}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserOrgAccessIntDto implements Serializable {
    String isActive;
    Integer tenantId;
    Integer userId;
    @NotNull
    Integer orgId;
    String orgName;
    String orgWards;
    String orgPhone;
    String orgCode;
    String isAssign;

    @JsonView(JsonViewUserDto.viewJsonIntUser.class)
    List<PosTerminalOrgAccessIntDto> posTerminalOrgAccessDtos;

    List<UserOrgWarehouseAccessIntDto> orgWarehouseAccessIntDtos;
}