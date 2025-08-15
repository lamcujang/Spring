package org.common.dbiz.dto.paymentDto.einvoice;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HiloAuthDto {

    String taxCode;
    String userName;
    String password;
    String pattern;
    String serial;
}
