package org.common.dbiz.dto.integrationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SyncOrderInfoDto {

    Integer tenantId;
    Integer orgId;
    Integer posOrderId;
    Integer userId;
    Integer kitchenOrderId;

}
