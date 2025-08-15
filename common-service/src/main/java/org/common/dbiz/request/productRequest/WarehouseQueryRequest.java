package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseQueryRequest extends BaseQueryRequest implements Serializable {

        private String code;
        private String name;
        private String isActive;
        private Integer orgId;
        private String keyword;
}
