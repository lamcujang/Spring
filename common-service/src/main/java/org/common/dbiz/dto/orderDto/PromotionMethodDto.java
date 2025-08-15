package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.common.dbiz.dto.productDto.ProductCategoryDto;
import org.common.dbiz.dto.productDto.ProductDto;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.PromotionMethod}
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromotionMethodDto implements Serializable {

    Integer id;
    Integer promotionId;

    BigDecimal totalAmount;

    Integer productId;
    String productName;
    Integer productCategoryId;
    String productCategoryName;
    ProductDto condProduct;
    ProductCategoryDto condCategory;
    BigDecimal productQty;

    Integer promoProductId;
    String promoProductName;
    Integer promoCategoryId;
    String promoCategoryName;
    ProductDto promoProduct;
    ProductCategoryDto promoCategory;

    BigDecimal promoProductQty; // KM Đơn hàng: Tặng hàng           // KM Hàng hóa: Tặng hàng, Giảm giá hàng khác
    BigDecimal promoPercent;    // KM Đơn hàng: Giảm giá đơn hàng   // KM Hàng hóa: Giảm giá hàng khác
    BigDecimal promoAmount;     // KM Đơn hàng: Giảm giá đơn hàng   // KM Hàng hóa: Giảm giá hàng khác
    BigDecimal percentMaxAmt;   // KM Đơn hàng: Giảm giá đơn hàng   // KM Hàng hóa:
    BigDecimal salesPrice;      // KM Đơn hàng:                     // KM Hàng hóa: Giảm giá đồng giá
    BigDecimal pointQty;        // KM Đơn hàng: Tặng điểm           // KM Hàng hóa: Tặng điểm

    String isAllCategory;
    String isAllPromoCategory;

}
