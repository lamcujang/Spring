package com.dbiz.app.orderservice.helper.promotion;

import org.common.dbiz.dto.orderDto.PromotionDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface GetApplicablePromoStrategy {

    List<PromotionDto> getApplicablePromo(
            Map<Integer, BigDecimal> qtyByProduct,
            Map<Integer, BigDecimal> qtyByCategory,
            BigDecimal totalAmount,
            List<PromotionDto> promotionDtoList
    );

}
