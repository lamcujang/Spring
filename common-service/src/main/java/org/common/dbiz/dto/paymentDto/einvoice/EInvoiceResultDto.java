package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EInvoiceResultDto {

    String supplierTaxCode;
    String invoiceNo;
    String transactionID;
    String reservationCode;
    String codeOfTax;
    String searchCode;
}
