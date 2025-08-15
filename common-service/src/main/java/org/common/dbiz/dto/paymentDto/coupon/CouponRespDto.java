package org.common.dbiz.dto.paymentDto.coupon;

import lombok.*;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.VendorDto;

import java.io.Serializable;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CouponRespDto implements Serializable {

    Integer id;
    Integer tenantId;
    Integer orgId;
    String orgName;
    String code;
    BigDecimal balanceAmount;
    String description;
    String isAvailable;
    String isActive;
    PosTerminalDto posTerminal;
    CustomerDto customer;
    VendorDto vendor;
}
