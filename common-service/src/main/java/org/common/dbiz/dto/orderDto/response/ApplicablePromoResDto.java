package org.common.dbiz.dto.orderDto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.orderDto.PromotionDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplicablePromoResDto implements Serializable {

    BigDecimal originalTotal;
//    BigDecimal originalTotalQty;

    List<PromotionDto> promotionList;

//    BigDecimal finalTotal;
//    BigDecimal totalDiscount;

}
