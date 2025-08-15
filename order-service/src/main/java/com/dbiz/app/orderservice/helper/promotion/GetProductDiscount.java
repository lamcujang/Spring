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
import java.math.RoundingMode;
import java.util.*;

@Component(AppConstant.PromotionType.BUY_ONE_DISCOUNT)
@Order(30)
@Slf4j
@RequiredArgsConstructor
public class GetProductDiscount implements GetApplicablePromoStrategy {

    private final PromotionHelper promotionHelper;

    @Override
    public List<PromotionDto> getApplicablePromo(
            Map<Integer, BigDecimal> qtyByProduct,
            Map<Integer, BigDecimal> qtyByCategory,
            BigDecimal totalAmount,
            List<PromotionDto> promotionDtoList) {

//        log.info("Calculating Product Promo - Buy one Discount"); // Khuyết mãi Hàng hóa - Mua hàng này giảm giá hàng khác

        List<PromotionDto> promotionDtoResultList = new ArrayList<>();
        for (PromotionDto promotionDto : promotionDtoList) {
            promotionDto.setIsApplicable("Y");
            promotionDto.setPromoBenefits(new ArrayList<>());
            for (PromotionMethodDto line : promotionDto.getPromotionMethodDto()) {
                // Get actualQty
                BigDecimal requiredQty = line.getProductQty();
                Integer productId = line.getProductId();
                Integer categoryId = line.getProductCategoryId();
                BigDecimal actualQty;
                if (productId != null) {
                    actualQty = qtyByProduct.getOrDefault(productId, BigDecimal.ZERO);
                } else if (categoryId != null) {
                    actualQty = qtyByCategory.getOrDefault(categoryId, BigDecimal.ZERO);
                } else {
                    throw new PosException("Promotion " + promotionDto.getId() + " of type " + promotionDto.getPromotionType() + " does not have either productId or categoryId");
                }
                if (actualQty.compareTo(requiredQty) < 0) { // if any condition fails, the whole promo doesn't apply
                    promotionDto.setIsApplicable("N");
                    promotionDto.setPromoBenefits(null);
                    break;
                }

                // Get actualPromoQty
                BigDecimal promoQty = line.getPromoProductQty();
                BigDecimal actualPromoQtySame = null;
                BigDecimal actualPromoQtyDiff = null;
                if ("Y".equals(promotionDto.getIsScaleWithQty())) {
                    actualPromoQtySame = productId != null ? // only return actualPromoQtySame if promoCond is product
                            promotionHelper.getSameProdPromoQty(actualQty, requiredQty, promoQty) :
                            null;
                    actualPromoQtyDiff = promotionHelper.getDiffProdPromoQty(actualQty, requiredQty, promoQty);
                }

                // Build promoBenefit
                PromoBenefit lineDto = PromoBenefit.builder()
                        .promotionId(promotionDto.getId())
                        .promotionBasedOn(promotionDto.getPromotionBasedOn())
                        .promotionBasedOnName(promotionDto.getPromotionBasedOnName())
                        .promotionType(promotionDto.getPromotionType())
                        .promotionTypeName(promotionDto.getPromotionTypeName())
                        .promotionMethodId(line.getId())

                        .productId(line.getProductId())
                        .productCategoryId(line.getProductCategoryId())
                        .condProduct(line.getCondProduct())
                        .condCategory(line.getCondCategory())

                        .promoProductId(line.getPromoProductId())
                        .promoCategoryId(line.getPromoCategoryId())
                        .promoProduct(line.getPromoProduct())
                        .promoCategory(line.getPromoCategory())

                        .isScaleWithQty(promotionDto.getIsScaleWithQty())
                        .productQty(line.getProductQty())
                        .promoProductQty(line.getPromoProductQty())
                        .actualPromoQtySame(actualPromoQtySame)
                        .actualPromoQtyDiff(actualPromoQtyDiff)

                        .promoPercent(line.getPromoPercent())
                        .promoAmount(line.getPromoAmount())
                        .build();
                promotionDto.getPromoBenefits().add(lineDto);
            }
            promotionDtoResultList.add(promotionDto);
        }
        return promotionDtoResultList;

    }

}
