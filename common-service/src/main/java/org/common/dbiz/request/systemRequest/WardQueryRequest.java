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
public class WardQueryRequest extends BaseQueryRequest {

    Integer id;
    Integer provinceId;

    String keyword;
    String code;
    String name;

    String hasMerger;
    String mergerDetails;
//    String administrativeCenter;

}
