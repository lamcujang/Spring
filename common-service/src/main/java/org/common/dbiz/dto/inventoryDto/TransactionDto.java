package org.common.dbiz.dto.inventoryDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.orderDto.ProductionDto;
import org.common.dbiz.dto.productDto.ProductLocationDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransactionDto {

    Integer orgId;

    List<Integer> productionIds;
    List<Integer> purchaseOrderIds;
    List<Integer> returnOrderIds;
    List<Integer> posOrderIds;
    List<Integer> productLocationIds;
    List<Integer> kitchenOrderLineIds;
    List<ProductLocationDto> productLocationDtos;
    List<ProductionDto> productionDtos;

}
