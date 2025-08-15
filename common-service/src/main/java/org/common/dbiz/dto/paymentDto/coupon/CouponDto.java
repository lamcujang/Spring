package org.common.dbiz.dto.paymentDto.coupon;

import lombok.*;
import org.common.dbiz.dto.paymentDto.BaseDto;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.paymentservice.domain.Coupon}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CouponDto extends BaseDto implements Serializable {

    String code;
    BigDecimal balanceAmount;
    String description;
    Integer posTerminalId;
    String isAvailable;
    Integer vendorId;
    Integer customerId;
    Integer erpCouponId;

}