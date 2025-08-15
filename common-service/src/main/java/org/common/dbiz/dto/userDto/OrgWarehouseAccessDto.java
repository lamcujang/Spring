package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.OrgWarehouseAccess}
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgWarehouseAccessDto implements Serializable {
    Integer tenantId;
    Integer orgId;
    Integer warehouseId;
    Integer userId;
    String isActive;
    String nameWarehouse;
    String warehouseCode;
    String orgName;
    String warehouseName;
    String isAssign;
}