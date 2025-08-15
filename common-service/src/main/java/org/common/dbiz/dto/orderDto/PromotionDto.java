package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.common.dbiz.dto.userDto.UserDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.Promotion}
 */

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromotionDto implements Serializable {

    Integer id;
    UserDto user;
    @Size(max = 32)
    String code;
    @Size(max = 255)
    String name;
    String isActive;

    String promotionBasedOn;
    String promotionBasedOnName;
    String promotionType;
    String promotionTypeName;

    @Size(max = 1)
    String isApplyBirthday;
    String isWarnIfUsed;
    BigDecimal qty;

    String fromDate;
    String toDate;
    String byDate;
    String byMonth;
    String excludedDate;

    String isAllOrg;
    String isAllBpartner;
    String isScaleWithQty;

    List<PromotionMethodDto> promotionMethodDto;

    List<PromotionTimeDto> promotionTimeDto;

    List<PromoAssignOrgDto> promoAssignOrgDto;

    List<PromoBpGroupDto> promoBpGroupDto;

    // Func: Get applicable promo
    String isApplicable;
    String HasUsed; // hiện chưa xử lý // nếu khách từng sử dụng chương trình KM này. Y-N nếu có bật cảnh báo, null nếu ko bật.
    List<PromoBenefit> promoBenefits;

}
