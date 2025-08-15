package org.common.dbiz.dto.paymentDto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CheckQRCodeMBBDto {

    Integer orgId;
    Integer posOrderId;
}
