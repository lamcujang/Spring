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
public class PriceListOrgQueryRequest extends BaseQueryRequest implements Serializable {

        private String id;
        private String isActive;
        private String isAll;
        private Integer pricelistId;
        private Integer orgId;
}
