package org.common.dbiz.dto.paymentDto.response;


import lombok.*;
import org.common.dbiz.request.BaseQueryRequest;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EInvoiceSetUpResDto {

    Integer id;
    Integer orgId;
    String value;
    String name;
    String description;
    String einvoicePartner;
    String isActive;
    String imageUrl;
    String isDefault;
}
