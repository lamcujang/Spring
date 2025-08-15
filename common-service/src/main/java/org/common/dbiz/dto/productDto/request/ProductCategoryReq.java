package org.common.dbiz.dto.productDto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryReq extends BaseQueryRequest {
    private Integer productCategoryId;
    Integer userId;
    Integer orgId;
    String orgName;
    String orgWards;
    String orgPhone;
    String orgCode;
    String isAssign;
    String keyword;
}