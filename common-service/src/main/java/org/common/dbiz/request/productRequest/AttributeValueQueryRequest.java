package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
public class AttributeValueQueryRequest extends BaseQueryRequest {
        Integer orgId;
}
