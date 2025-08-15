package org.common.dbiz.dto.inventoryDto;

import lombok.*;
import org.common.dbiz.dto.ParamDto;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransactionParamDto  {
    private Integer tenantId;
    private Integer orgId;
    private int page=0;
    private int pageSize=15;
    private String order="desc";
    private String sortBy="transaction_date";
    Integer productId;
}
