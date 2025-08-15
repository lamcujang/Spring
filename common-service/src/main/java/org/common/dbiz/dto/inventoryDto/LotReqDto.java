package org.common.dbiz.dto.inventoryDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LotReqDto extends BaseQueryRequest {

    Integer id;
    Integer tenantId;
    Integer orgId;
    String code;
    Integer productId;
    Integer warehouseId;
    Integer locatorId;
    Integer vendorId;
    String expirationDateFrom;
    String expirationDateTo;
    String manufactureDateFrom;
    String manufactureDateTo;
    String description;
    String lotStatus;
    String isActive;
    BigDecimal costPrice;
    String created;
    String updated;
}
