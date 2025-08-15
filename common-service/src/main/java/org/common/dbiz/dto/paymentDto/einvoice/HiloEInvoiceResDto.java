package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HiloEInvoiceResDto {

    String lstXmlData;
    String Code;
    Boolean success;
    String error;
    String messages;
    Object data;
    Object result;
}
