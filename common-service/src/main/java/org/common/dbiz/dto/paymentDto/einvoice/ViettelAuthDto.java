package org.common.dbiz.dto.paymentDto.einvoice;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ViettelAuthDto {

    String taxCode;
    String password;
}
