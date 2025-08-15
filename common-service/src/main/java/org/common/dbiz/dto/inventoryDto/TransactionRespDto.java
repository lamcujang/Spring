package org.common.dbiz.dto.inventoryDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ProductCDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.dto.tenantDto.OrgDto;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransactionRespDto {

    Integer id;
    OrgDto org;
    Integer tenantId;
    String transactionType;
    String transactionTypeName;
    String isActive;
    BigDecimal qty;
    String transactionDate;
    String userName;
    String userFullName;
    WarehouseDto warehouse;
    ProductCDto product;
}
