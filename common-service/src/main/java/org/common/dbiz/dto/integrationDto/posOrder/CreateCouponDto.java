package org.common.dbiz.dto.integrationDto.posOrder;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateCouponDto {
    private Integer ad_Org_ID;
    private String couponCode;
    private BigDecimal couponAmt;
    private Integer C_POS_ID;
    private Integer C_BPartner_ID;
    private Integer C_BPartner2_ID;
    private String isActive;
    private String isAvailable;

}
