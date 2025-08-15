package org.common.dbiz.dto.reportDto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WarehouseAccessDto {
    Integer warehouseId;
    Integer orgId;
    Integer posTerminalId;
    String namePosTerminal;
    String description;
    String address;
    String code;
}
