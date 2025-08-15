package com.dbiz.app.orderservice.helper.promotion;

import com.dbiz.app.orderservice.constant.AppConstant;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PromotionDto;
import org.common.dbiz.dto.orderDto.PromotionMethodDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class PromotionHelper {

//    BigDecimal a,b;
//    these are equivalent:
//    a.divide(b, 0, RoundingMode.DOWN);
//    a.divideToIntegralValue(b);

    public BigDecimal getSameProdPromoQty(BigDecimal actualQty, BigDecimal requiredQty, BigDecimal promoQty) {
        // bundle size = paid + free per bundle
        BigDecimal bundleSize = requiredQty.add(promoQty);
        // how many full bundles?
        BigDecimal numBundles = actualQty.divideToIntegralValue(bundleSize);

//        // Option A: allow partial promoQty
//        // free items from full bundles
//        BigDecimal freebiesFromBundles = numBundles.multiply(promoQty);
//        // what's left after full bundles?
//        BigDecimal consumedByBundles = numBundles.multiply(bundleSize);
//        BigDecimal leftover = actualQty.subtract(consumedByBundles);
//        // free items from leftover = max(0, leftover - requiredQty)
//        BigDecimal freebiesFromLeftover =
//                leftover.subtract(requiredQty)
//                        .max(BigDecimal.ZERO);
//        return freebiesFromBundles.add(freebiesFromLeftover);

        // Option B: allow full bundle only
        return numBundles.multiply(promoQty);
    }

    public BigDecimal getDiffProdPromoQty(BigDecimal actualQty, BigDecimal requiredQty, BigDecimal promoQty) {
        // allow full bundle only
        // floor ( actualQty / requiredQty ) * promoQty
        return actualQty.divideToIntegralValue(requiredQty).multiply(promoQty);
    }

//    // OUTDATED: Check of promotion applicable based on promotionType (maybe extract checking from stratImpl into this in the future)
//
//    private static final Set<String> MULTI_CONDITION_PROD_PROMO_TYPES = Set.of(
//            AppConstant.PromotionType.FREE_WITH_GIFT,
//            AppConstant.PromotionType.BUY_ONE_DISCOUNT);
//
//    private static final Set<String> TIERED_CONDITION_PROD_PROMO_TYPES = Set.of(
//            AppConstant.PromotionType.PRODUCT_DISCOUNT_ORDER);
//
//    public boolean isProductPromoApplicable(Map<Integer, BigDecimal> qtyByProduct,
//                                             Map<Integer, BigDecimal> qtyByCategory,
//                                             PromotionDto promotionDto) {
//
//        if (MULTI_CONDITION_PROD_PROMO_TYPES.contains(promotionDto.getPromotionType())) {
//            // every “line” is a required condition for this promo
//            for (PromotionMethodDto line : promotionDto.getPromotionMethodDto()) {
//                BigDecimal requiredQty = line.getProductQty();
//                Integer productId = line.getProductId();
//                Integer categoryIdId = line.getProductCategoryId();
//
//                BigDecimal actualQty;
//                if (productId != null) {
//                    actualQty = qtyByProduct.getOrDefault(productId, BigDecimal.ZERO);
//                } else {
//                    actualQty = qtyByCategory.getOrDefault(categoryIdId, BigDecimal.ZERO);
//                }
//
//                // if any condition fails, the whole promo doesn't apply
//                if (actualQty.compareTo(requiredQty) < 0) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        if (TIERED_CONDITION_PROD_PROMO_TYPES.contains(promotionDto.getPromotionType())) {
//            // use productId of 1 of the lines to load actualQty (Because in tiered promo, all lines are condition of the same product (not category) qty)
//            BigDecimal actualQty = qtyByProduct.getOrDefault(
//                    AppConstant.PromotionType.PRODUCT_DISCOUNT_ORDER.equals(promotionDto.getPromotionType()) ?
//                            promotionDto.getPromotionMethodDto().get(0).getProductId() :
//                            null,
//                    BigDecimal.ZERO);
//            // pick the best tier rather than require them all
//            Optional<PromotionMethodDto> bestTier = promotionDto.getPromotionMethodDto().stream()
//                    .filter(line -> actualQty.compareTo(line.getProductQty()) >= 0)
//                    .max(Comparator.comparing(PromotionMethodDto::getProductQty));
//            if (bestTier.isEmpty())
//                return false;
//            else
//                return true;
//            // bestTier is not being returned?
//            // then apply bestTier’s benefit only
//        }
//        return false; // don't apply not recognized types
//
//    }

}
