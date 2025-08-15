package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.dbiz.app.domain.Role}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoleDto implements Serializable {
    String isActive;
    Integer id;
    @Size(max = 32)
    String code;
    @Size(max = 255)
    String name;
    private Integer erpRoleId;
    private String created;
    String updated;
    String description;
    String routeFunction;
    Integer copyFromRoleId;
    String fullRole;
}