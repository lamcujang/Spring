package org.common.dbiz.dto.orderDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GeneratePOSBillNumberDto {

    Integer posTerminalId;
    String floorNo;
    String tableNo;
    Integer posOrderId;
}
