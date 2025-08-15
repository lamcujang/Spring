package com.dbiz.app.orderservice.helper.promotion;

import com.dbiz.app.orderservice.constant.AppConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PromoBenefit;
import org.common.dbiz.dto.orderDto.PromotionDto;
import org.common.dbiz.dto.orderDto.PromotionMethodDto;
import org.common.dbiz.exception.PosException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component(AppConstant.PromotionType.PRODUCT_DISCOUNT_ORDER)
@Order(40)
@Slf4j
@RequiredArgsConstructor
public class GetProductFlatDiscount implements GetApplicablePromoStrategy {

    @Override
    public List<PromotionDto> getApplicablePromo(
            Map<Integer, BigDecimal> qtyByProduct,
            Map<Integer, BigDecimal> qtyByCategory,
            BigDecimal totalAmount,
            List<PromotionDto> promotionDtoList) {

//        log.info("Calculating Product Promo - Product Discount Order"); // Khuyết mãi Hàng hóa - Khuyết mãi giảm giá / đồng giá

        List<PromotionDto> promotionDtoResultList = new ArrayList<>();
        for (PromotionDto promotionDto : promotionDtoList) {
            // Get productId
            Integer productId = promotionDto.getPromotionMethodDto().get(0).getProductId(); // Giảm giá đồng giá: Các line là các mức giảm giá => Đều cùng productId
            BigDecimal actualQty;
            if (productId != null) {
                actualQty = qtyByProduct.getOrDefault(productId, BigDecimal.ZERO);
            } else {
                throw new PosException("Promotion " + promotionDto.getId() + " of type " + promotionDto.getPromotionType() + " does not have productId");
            }
            // Calculate best line
            Optional<PromotionMethodDto> optBestLine = promotionDto.getPromotionMethodDto().stream()
                    .filter(line -> actualQty.compareTo(line.getProductQty()) >= 0) // order actualQty equal or more than conditionQty
                    .min(Comparator.comparing(PromotionMethodDto::getSalesPrice)); // lowest salesPrice among those
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

                    .productId(bestLine.getProductId())
                    .productCategoryId(bestLine.getProductCategoryId())
                    .condProduct(bestLine.getCondProduct())
                    .condCategory(bestLine.getCondCategory())

                    // maybe Promo fields are null
                    .promoProductId(bestLine.getPromoProductId())
                    .promoCategoryId(bestLine.getPromoCategoryId())
                    .promoProduct(bestLine.getPromoProduct())
                    .promoCategory(bestLine.getPromoCategory())

                    .productQty(bestLine.getProductQty())
                    .salesPrice(bestLine.getSalesPrice())
                    .build();
            promotionDto.setIsApplicable("Y");
            promotionDto.setPromoBenefits(new ArrayList<>());
            promotionDto.getPromoBenefits().add(lineDto);

            promotionDtoResultList.add(promotionDto);
        }
        return promotionDtoResultList;

    }

}
