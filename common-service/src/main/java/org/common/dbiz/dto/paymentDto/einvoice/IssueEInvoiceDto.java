package org.common.dbiz.dto.paymentDto.einvoice;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class IssueEInvoiceDto {

    Integer posOrderId;
    Integer invoiceId;
    Integer tenantId;
    String priceCateCode;
}
