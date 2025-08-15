package org.common.dbiz.dto.tenantDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateTenantRespDto {

    Integer id;
    String tenantCode;
    String tenantName;
    String industryCode;
    String ownerUserName;
    String ownerPassword;
    String domainUrl;
}
