package org.common.dbiz.request.tenantRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;


@Data
@AllArgsConstructor
public class PosTerminalQueryRequest extends BaseQueryRequest implements Serializable {

    Integer id;
    Integer tenantId;
    Integer orgId;
    String isActive;

    String keyword;
}
