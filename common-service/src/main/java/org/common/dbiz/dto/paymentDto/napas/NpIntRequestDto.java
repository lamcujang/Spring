package org.common.dbiz.dto.paymentDto.napas;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NpIntRequestDto {

    Integer id;
    Integer orgId;
    Integer posTerminalId;
    Integer bankAccountId;
    String phone;
    String requestStatus;

}
