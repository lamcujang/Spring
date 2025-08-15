package org.common.dbiz.dto.paymentDto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankNpReqDto extends BaseQueryRequest {

    String keyword;

    String code;
    String name;
    String shortName;
    String binCode;
    String swiftCode;

}
