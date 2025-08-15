package com.dbiz.app.orderservice.helper.promotion;

import com.dbiz.app.orderservice.constant.AppConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PromoBenefit;
import org.common.dbiz.dto.orderDto.PromotionDto;
import org.common.dbiz.dto.orderDto.PromotionMethodDto;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component(AppConstant.PromotionType.GIVE_WITH_PURCHASE)
@Order(50)
@Slf4j
@RequiredArgsConstructor
public class GetOrderGift implements GetApplicablePromoStrategy {

    @Override
    public List<PromotionDto> getApplicablePromo(
            Map<Integer, BigDecimal> qtyByProduct,
            Map<Integer, BigDecimal> qtyByCategory,
            BigDecimal totalAmount,
            List<PromotionDto> promotionDtoList) {

//        log.info("Calculating Order Promo - Give with Purchase"); // Khuyết mãi Đơn hàng - Khuyết mãi tặng hàng

        List<PromotionDto> promotionDtoResultList = new ArrayList<>();
        for (PromotionDto promotionDto : promotionDtoList) {
            // Calculate best line
            Optional<PromotionMethodDto> optBestLine = promotionDto.getPromotionMethodDto().stream()
                    .filter(line -> totalAmount.compareTo(line.getTotalAmount()) >= 0) // orderTotal equal or more than conditionTotal
                    .max(Comparator.comparing(PromotionMethodDto::getTotalAmount)); // highest among those
            if (optBestLine.isEmpty()) { // no line match
                promotionDto.setIsApplicable("N");
                promotionDtoResultList.add(promotionDto);
                continue;
            }
            PromotionMethodDto bestLine = optBestLine.get();

            // Build promoBenefit
            PromoBenefit lineDto = PromoBenefit.builder()
                    .promotionId(promotionDto.getId())
                    .promotionBasedOn(promotionDto.getPromotionBasedOn())
                    .promotionBasedOnName(promotionDto.getPromotionBasedOnName())
                    .promotionType(promotionDto.getPromotionType())
                    .promotionTypeName(promotionDto.getPromotionTypeName())
                    .promotionMethodId(bestLine.getId())

                    .totalAmount(bestLine.getTotalAmount())

                    .promoProductId(bestLine.getPromoProductId())
                    .promoCategoryId(bestLine.getPromoCategoryId())
                    .promoProduct(bestLine.getPromoProduct())
                    .promoCategory(bestLine.getPromoCategory())

                    .promoProductQty(bestLine.getPromoProductQty())
                    .build();
            promotionDto.setIsApplicable("Y");
            promotionDto.setPromoBenefits(new ArrayList<>());
            promotionDto.getPromoBenefits().add(lineDto);

            promotionDtoResultList.add(promotionDto);
        }
        return promotionDtoResultList;

    }

}
