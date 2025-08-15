package org.common.dbiz.dto.paymentDto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentMethodDto {

    private Integer id;
    private String paymentName;
    private String code;
}
