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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.Math;
import java.util.Objects;

@Component(AppConstant.PromotionType.FREE_WITH_GIFT)
@Order(20)
@Slf4j
@RequiredArgsConstructor
public class GetProductGift implements GetApplicablePromoStrategy {

    private final PromotionHelper promotionHelper;

    @Override
    public List<PromotionDto> getApplicablePromo(
            Map<Integer, BigDecimal> qtyByProduct,
            Map<Integer, BigDecimal> qtyByCategory,
            BigDecimal totalAmount,
            List<PromotionDto> promotionDtoList) {

//        log.info("Calculating Product Promo - Free with gift"); // Khuyết mãi Hàng hóa - Khuyết mãi tặng hàng

        List<PromotionDto> promotionDtoResultList = new ArrayList<>();
//        promoLoop:
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
//                    promotionDto.getPromoBenefits().clear();

//                    promotionDtoResultList.add(promotionDto);
//                    continue promoLoop;
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
                        .build();
                promotionDto.getPromoBenefits().add(lineDto);
            }
            promotionDtoResultList.add(promotionDto);
        }
        return promotionDtoResultList;

    }

}

//----------------------------------------------------------------------------------------------------------------------
//boolean isCondProd     = line.getProductId()           != null && line.getProductCategoryId() == null;
//boolean isCondCat      = line.getProductId()           == null && line.getProductCategoryId() != null;
//boolean isPromoProd    = line.getPromoProductId()      != null && line.getPromoCategoryId() == null;
//boolean isPromoCat     = line.getPromoProductId()      == null && line.getPromoCategoryId() != null;
//if ("N".equals(promotionDto.getIsScaleWithQty())) {
//    if (isCondProd) {
//        if (isPromoProd) {
//            // code
//        } else if (isPromoCat) {
//            // code
//        }
//    } else if (isCondCat) {
//        if (isPromoCat) {
//            // code
//        } else if (isPromoCat) {
//            // code
//        }
//    }
//} else if ("Y".equals(promotionDto.getIsScaleWithQty())) {
//    if (isCondProd) {
//        if (isPromoProd) {
//            // code
//        } else if (isPromoCat) {
//            // code
//        }
//    } else if (isCondCat) {
//        if (isPromoCat) {
//            // code
//        } else if (isPromoCat) {
//            // code
//        }
//    }
//}
//----------------------------------------------------------------------------------------------------------------------
//                PosOrderLineVAllDto lineDto = PosOrderLineVAllDto.builder()
////                        .salesPrice(BigDecimal.valueOf(1000000)) // chưa query lên
//                        .priceDiscount(BigDecimal.ZERO)
//                        .actualPromoQty( // PENDING: chưa xử lý case cùng product
//                                "Y".equals(promotionDto.getIsScaleWithQty()) ?
//                                        BigDecimal.valueOf(Math.floor(actualQty.divide(requiredQty, RoundingMode.HALF_DOWN).doubleValue())).multiply(promoQty) :
//                                        promoQty
//                        )
////                        .lineBaseAmt(wefsdafsdafasdf) // cần salesPrice
//                        .lineNetAmt(BigDecimal.ZERO)
//                        .build();
//                promotionDtoResult.getPromoBenefitProductList().add(lineDto);
//----------------------------------------------------------------------------------------------------------------------
