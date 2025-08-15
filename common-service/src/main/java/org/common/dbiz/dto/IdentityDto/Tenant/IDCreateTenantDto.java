package org.common.dbiz.dto.IdentityDto.Tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.IdentityDto.User.IDOwnerDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IDCreateTenantDto {

    String domain;
    List<IDOwnerDto> owners;
}
