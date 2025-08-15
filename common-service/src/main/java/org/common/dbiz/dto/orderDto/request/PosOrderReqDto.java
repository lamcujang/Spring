package org.common.dbiz.dto.orderDto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PosOrderReqDto {

    Integer id;
    String isBillInfo;
}
