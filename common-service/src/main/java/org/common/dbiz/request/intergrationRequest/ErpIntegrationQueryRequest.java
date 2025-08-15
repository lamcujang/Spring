package org.common.dbiz.request.intergrationRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
public class ErpIntegrationQueryRequest extends BaseQueryRequest {
        String isDefault;
        String integrationType;
}