package org.common.dbiz.dto.paymentDto.coupon;


import lombok.*;
import org.common.dbiz.dto.ParamDto;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CouponParamDto extends ParamDto implements Serializable {

    String code;
    Integer posTerminalId;
    Integer vendorId;
    Integer customerId;
    String isAvailable;
    String isActive;
}
