package org.common.dbiz.dto.paymentDto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NpIntRequestReqDto extends BaseQueryRequest {

    Integer bankId;

}
