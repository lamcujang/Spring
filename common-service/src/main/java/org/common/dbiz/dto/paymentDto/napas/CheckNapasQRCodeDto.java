package org.common.dbiz.dto.paymentDto.napas;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CheckNapasQRCodeDto {
    Integer orgId;
    Integer posTerminalId;
    Integer posOrderId;
    String sitTest;
}
