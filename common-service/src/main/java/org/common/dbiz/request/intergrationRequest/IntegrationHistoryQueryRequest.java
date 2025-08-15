package org.common.dbiz.request.intergrationRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
public class IntegrationHistoryQueryRequest extends BaseQueryRequest {

    String dateHistory ;
    String intStatus;
    String intFlow;
    String intDateTo;
    String intDateFrom;
    String integrationType;
}