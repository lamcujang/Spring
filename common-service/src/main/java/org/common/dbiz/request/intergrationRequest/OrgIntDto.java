package org.common.dbiz.request.intergrationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.tenantDto.OrgDto;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgIntDto {
    List<OrgDto> listOrgInt;
    String type ;
}
