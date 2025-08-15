package org.common.dbiz.dto.paymentDto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BillInfoReqDto {

    Integer invoiceId; // dùng để lấy posOrderId;
    Integer posTerminalId;

}
