package org.common.dbiz.request.systemRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaxOfficeQueryRequest extends BaseQueryRequest {

    Integer id;

    String keyword;
    String code;
    String name;
    String oldName;

    Integer provinceId;

    String isActive;
    String isParent;

}
