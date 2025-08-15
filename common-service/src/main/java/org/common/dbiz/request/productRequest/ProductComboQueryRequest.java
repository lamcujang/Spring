package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductComboQueryRequest extends BaseQueryRequest

{
    String isItems;
    Integer productId;
    Integer productComponentId;
    Integer orgId;
    String isActive;
}
