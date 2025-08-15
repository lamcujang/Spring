package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.view.GetUserOrgAccessV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetUserOrgAccessVDto implements Serializable {
    Integer tenantId;
    Integer userId;
    Integer orgId;
    @Size(max = 1)
    String isActive;
    @Size(max = 255)
    String name;
    String address;
    String isPosMng;
}