package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EInvoiceResDto {

    String errorCode;
    String description;
    String message;
    Object data;
    Object result;
}
