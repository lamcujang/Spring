package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HiloEInvoiceResultDto {

    String pattern;
    String serial;
    String fKey;
    String searchKey;
    String key;
    String no;
    String TaxOfCode;
}
