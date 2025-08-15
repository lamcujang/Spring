package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.domain.UserRoleAccess}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRoleAccessDto implements Serializable {
    String isActive;
    private Integer userId;
    private Integer roleId;
    private String roleName;
    private String created;
    private String updated;
    private String description;
    private String isAssign;

}