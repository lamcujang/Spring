package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ProductCategoryDto;
import org.common.dbiz.dto.productDto.ProductDto;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromoBenefit {

    Integer promotionId;
    String promotionBasedOn;
    String promotionBasedOnName;
    String promotionType;
    String promotionTypeName;

    Integer promotionMethodId;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    String isNewLine; // Nếu là ProductPromo: Tặng hàng và Giảm giá hàng khác là new line, Giảm giá đồng giá ko new line

//    PosOrderLineVAllDto promoBenefitProductList; // dùng dto hoặc list fields
//    PosOrderDto promoBenefitOrderList; // dùng dto hoặc list fields

//    Integer conditionLineId; // line hàng hóa trg đơn kích hoạt KM

//    BigDecimal originalSalesPrice; // giá gốc
//    BigDecimal priceDiscount; // giá đã giảm
//    BigDecimal lineBaseAmt; // giá gốc * sl
//    BigDecimal lineNetAmt; // giá đã giảm * sl

    BigDecimal totalAmount;

    Integer productId;
    Integer productCategoryId;
    ProductDto condProduct;
    ProductCategoryDto condCategory;

    Integer promoProductId;
    Integer promoCategoryId;
    ProductDto promoProduct;
    ProductCategoryDto promoCategory;

    String isScaleWithQty;
    BigDecimal productQty;
    BigDecimal promoProductQty;
    // khác với promoQty: (KM Đồng giá: là qty sản phẩm), (KM Giảm giá hàng khác/Tặng hàng + Có tăng theo sl: scale lên)
    BigDecimal actualPromoQtySame;
    BigDecimal actualPromoQtyDiff;

    BigDecimal promoPercent;
    BigDecimal promoAmount;
    BigDecimal percentMaxAmt;
    String isExceedPercentMax;
    BigDecimal actualPromoAmount;
    BigDecimal salesPrice; // giá giảm đồng giá

}

///////////////////// LINE PROMOTION /////////////////////
//    PosOrderLineVAllDto promoBenefitProductList; // dùng dto hoặc list fields
//
////    Integer conditionLineId; // line hàng hóa trg đơn kích hoạt KM
//
//    BigDecimal promoProductQty;
//    BigDecimal actualPromoQty; // khác với promoQty: (KM Đồng giá: là qty sản phẩm), (KM Giảm giá hàng khác/Tặng hàng + Có tăng theo sl: scale lên)
//
//    Integer promoProductId;
//    //    String promoProductName;
//    Integer promoCategoryId;
////    String promoCategoryName;
//
//    BigDecimal promoPercent;
//    BigDecimal promoAmount;
//    BigDecimal salesPrice; // giá giảm đồng giá
//
////    BigDecimal originalSalesPrice; // giá gốc
////    BigDecimal priceDiscount; // giá đã giảm
////    BigDecimal lineBaseAmt; // giá gốc * sl
////    BigDecimal lineNetAmt; // giá đã giảm * sl
//
//////////////////// ORDER PROMOTION ////////////////////
//    PosOrderDto promoBenefitOrderList; // dùng dto hoặc list fields
//
////    String isExceedPercentMax; // ko dùng nếu ko tính
//    BigDecimal promoPercent;
//    BigDecimal promoAmount;
//    BigDecimal percentMaxAmt;
//
//    Integer promoProductId;
////    String promoProductName;
//    Integer promoCategoryId;
////    String promoCategoryName;
//
/////////////////////////////////////////////////////////