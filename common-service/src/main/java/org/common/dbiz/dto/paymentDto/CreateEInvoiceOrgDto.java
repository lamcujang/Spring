package org.common.dbiz.dto.paymentDto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateEInvoiceOrgDto {

    Integer id;
    Integer orgId;
    Integer einvoiceSetupId;
    String taxId;
    String password;
    String name;
    String description;
    String isDirectIssue;
    String isAttachNote;
    String invoiceSign;
    String invoiceForm;

}
