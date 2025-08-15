package org.common.dbiz.dto.paymentDto.einvoice;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GeneralInvoiceInfoDto {

    String invoiceSeries;
    String adjustmentType;
    String adjustmentInvoiceType;
    String cusGetInvoiceRight;
    String invoiceType;
    Long invoiceIssuedDate;
    String templateCode;
    String currencyCode;
    Long additionalReferenceDate;
    Boolean paymentStatus;
    String invoiceNote;
    String additionalReferenceDesc;
    String transactionUuid;
    String originalInvoiceId;
    String originalInvoiceType;
    String originalInvoiceIssueDate;
    String originalTemplateCode;
}
