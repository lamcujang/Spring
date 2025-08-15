package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageQueryRequest extends BaseQueryRequest {
    private String code;
    private String name;
    private String productType;
    private String taxCode;
    private BigDecimal salePriceFrom;
    private BigDecimal salePriceTo;
    private BigDecimal costPriceFrom;
    private BigDecimal costPriceTo;
    private String isPurchased;
    private Integer orgId;
    private Integer categoryId;
}
