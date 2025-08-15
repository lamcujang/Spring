package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQueryRequest extends BaseQueryRequest {
    String keyword;
    private String code;
    private String name;
    private String taxCode;
    private BigDecimal salePriceFrom;
    private BigDecimal salePriceTo;
    private BigDecimal costPriceFrom;
    private BigDecimal costPriceTo;
    private String isPurchased;
    private Integer orgId;
    private String categoryId;
    String orgKeyword;
    String productType;
    String isInCombo;
    String[] groupType;
    String brand;
    Integer brandId;
    Integer productId;
    Integer userId;
    String isActive;
    Integer warehouseId;
    String isTopping;
    String isAssign;
    List<Integer> notInIds;
    String searchKey;
}
