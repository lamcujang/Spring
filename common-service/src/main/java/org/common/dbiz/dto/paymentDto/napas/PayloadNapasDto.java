package org.common.dbiz.dto.paymentDto.napas;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PayloadNapasDto {

    HeaderPayloadNapasDto header;
    Object payload;
}
