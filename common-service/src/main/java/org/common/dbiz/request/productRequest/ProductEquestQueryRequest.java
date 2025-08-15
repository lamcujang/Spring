package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEquestQueryRequest extends BaseQueryRequest {
   private Integer orgId;
   private Integer tenantId;
   private Integer posTerminalId;
   private Integer productCategoryId;
   private Integer taxId;
   private String name;
   private Integer productId;
}
