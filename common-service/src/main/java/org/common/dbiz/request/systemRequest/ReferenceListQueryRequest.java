package org.common.dbiz.request.systemRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
public class ReferenceListQueryRequest extends BaseQueryRequest {
    Integer referenceId ;
    String value;
    String name ;
}