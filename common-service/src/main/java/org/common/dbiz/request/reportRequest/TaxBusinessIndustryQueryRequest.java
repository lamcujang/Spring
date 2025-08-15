package org.common.dbiz.request.reportRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxBusinessIndustryQueryRequest extends BaseQueryRequest {
    private Integer id;
    private String industryCode;
    private String industryName;
    private String isActive;
}
