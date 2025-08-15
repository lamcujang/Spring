package org.common.dbiz.dto.paymentDto.einvoice;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MetadataDto {

    String keyTag;
    String stringValue;
    String valueType;
    String keyLabel;
}
