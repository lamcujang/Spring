package org.common.dbiz.dto.paymentDto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QRCodeRespDto {

    String qrCode;
    String bankName;
    String accountNo;
    String bankOwner;
    String image;

}
