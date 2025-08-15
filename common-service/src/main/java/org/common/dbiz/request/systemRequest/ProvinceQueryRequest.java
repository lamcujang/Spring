package org.common.dbiz.request.systemRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProvinceQueryRequest extends BaseQueryRequest {

    Integer id;

    String keyword;
    String code;
    String name;
    String shortName;
    String shortCode;

    String placeType;

    String isMerged;
    String mergedWith;

}
