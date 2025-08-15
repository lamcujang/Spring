package org.common.dbiz.dto.integrationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.UserRoleAccess}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRoleAccessIntDto implements Serializable {
    String isActive;
    private Integer userId;
    private Integer roleId;
    private String roleName;
    private String created;
    private String updated;
    private String description;
    private String isAssign;

}