package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessSectorGroupQueryRequest extends BaseQueryRequest {

    private String name;
    private Integer code;
    private String isActive;
}
