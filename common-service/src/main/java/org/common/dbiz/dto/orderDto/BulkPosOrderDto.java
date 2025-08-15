package org.common.dbiz.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BulkPosOrderDto {

    Integer tenantId;
    List<PosOrderDto> data;
}
