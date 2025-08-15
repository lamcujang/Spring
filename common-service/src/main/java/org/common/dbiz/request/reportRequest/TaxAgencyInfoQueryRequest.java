package org.common.dbiz.request.reportRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxAgencyInfoQueryRequest extends BaseQueryRequest {
    private Integer id;
    private String taxAgentCode;
    private String isActive;
}
