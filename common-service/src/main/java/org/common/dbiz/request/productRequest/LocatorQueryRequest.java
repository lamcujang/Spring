package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocatorQueryRequest extends BaseQueryRequest {

        private String code;
        private String x;
        private String y;
        private String z;
        private Integer warehouseId;
        private String isActive;
        private Integer orgId;
}
