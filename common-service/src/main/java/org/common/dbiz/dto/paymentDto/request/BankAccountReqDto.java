package org.common.dbiz.dto.paymentDto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankAccountReqDto extends BaseQueryRequest {

    Integer bankId;

}
