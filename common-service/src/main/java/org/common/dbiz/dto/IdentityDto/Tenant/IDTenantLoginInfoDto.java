package org.common.dbiz.dto.IdentityDto.Tenant;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IDTenantLoginInfoDto {

    String ownerUserName;
    String ownerPassword;
    String code;
    String industryCode;

}
