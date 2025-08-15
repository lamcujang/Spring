package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.RolePermission}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RolePermissionVDto implements Serializable {
    String isActive;
    Integer id;
    Integer roleId;
    String name;
    String code;
    Integer tenantId;
    JsonNode permissions;
    String description ;
}