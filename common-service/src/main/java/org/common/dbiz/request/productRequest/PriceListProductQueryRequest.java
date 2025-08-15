package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceListProductQueryRequest extends BaseQueryRequest implements Serializable {

        private String productId;
        private String isActive;
        private BigDecimal costprice;
        private BigDecimal standardprice;
        private BigDecimal salesprice;
        private BigDecimal lastorderprice;

        private Integer priceListId;
        private Integer productCategoryId;
        private String keyword;
        private Integer orgId;
}
