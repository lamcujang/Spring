package org.common.dbiz.dto.paymentDto.request;


import lombok.*;
import org.common.dbiz.request.BaseQueryRequest;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EInvoiceSetUpReqDto extends BaseQueryRequest {

    String value;
    String name;
    String description;
    String einvoicePartner;

}
