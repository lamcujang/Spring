package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.view.GetUserRoleAccessV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetUserRoleAccessVDto implements Serializable {
    Integer tenantId;
    Integer userId;
    Integer roleId;
    @Size(max = 1)
    String isActive;
    @Size(max = 255)
    String name;
    String code;
    private String routeFunction;

}