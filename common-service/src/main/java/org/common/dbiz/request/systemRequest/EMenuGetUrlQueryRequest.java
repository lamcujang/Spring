package org.common.dbiz.request.systemRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
public class EMenuGetUrlQueryRequest extends BaseQueryRequest {
    Integer orgId;
    Integer posTerminalId;
    Integer floorId;
    Integer tableId;
    String keyWords;
}