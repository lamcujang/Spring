package org.common.dbiz.dto.orderDto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.orderDto.PosOrderLineVAllDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplicablePromoReqDto extends BaseQueryRequest implements Serializable {

    Integer orgId;
    Integer customerId;
//    Integer bpGroupId;

    List<PosOrderLineVAllDto> products;
//    class PosOrderLineVAllDto {
//        Integer productId;
//        Integer productCategoryId;
//        BigDecimal saleprice;
//        BigDecimal qty;
//        Integer promotionId;
//    }

    BigDecimal totalAmount; // truyền hoặc tự tính
    String orderDate; // yyyy-MM-dd HH:mm:ss

}
