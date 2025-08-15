package org.common.dbiz.dto.productDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductOrgAssignDto {

    String isActive;
    Integer tenantId;
    Integer userId;
    Integer orgId;
    String orgName;
    String orgWards;
    String orgPhone;
    String orgCode;
    String isAssign;

}
