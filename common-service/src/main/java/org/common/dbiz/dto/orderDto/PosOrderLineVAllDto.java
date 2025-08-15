package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.common.dbiz.dto.inventoryDto.LotDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.view.PosOrderLineVAll}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PosOrderLineVAllDto implements Serializable {

    Integer tenantId;
    Integer orgId;
    Integer id;
    BigDecimal qty;
    @Size(max = 64)
    String status;
    @Size(max = 15)
    String valueStatus;
    @Size(max = 255)
    String description;
    Integer productId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer productCategoryId; // for promotion
    Integer posOrderId;
    Integer taxId;
    String isActive;
    BigDecimal extraAmount;
    Integer kitchenOrderLineId;
    Integer posOrderLineId;
    Integer requestOrderLineId;

    BigDecimal salesPrice;
    BigDecimal discountPercent;
    BigDecimal priceDiscount;
    BigDecimal discountAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    BigDecimal lineBaseAmt;
    BigDecimal lineNetAmt;
    BigDecimal taxAmount;
    BigDecimal grandTotal;

    List<Integer> promotionIds; // tuỳ rule: các PromoLine cùng Product của những Promo khác nhau có gợp lại hay ko
    BigDecimal promoPercent;
    BigDecimal promoAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    BigDecimal actualPromoQty;

    String timeWaiting; // dung cho request order
    private BigDecimal taxRate;
    private String taxName;
    ProductPosOrderLineVAllDto productDto;
    List<PosOrderLineDetailDto> lineDetails;
    List<PosLotDto> lots;
    String created;
}