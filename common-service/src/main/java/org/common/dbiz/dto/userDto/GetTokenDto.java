package org.common.dbiz.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetTokenDto {

    private String username;
    private String password;
    private Integer d_tenant_id;
    private Integer d_org_id;
    private String language;
    private Integer userId;
}
