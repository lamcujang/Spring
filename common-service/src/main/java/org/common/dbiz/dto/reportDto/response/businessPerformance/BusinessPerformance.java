package org.common.dbiz.dto.reportDto.response.businessPerformance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BusinessPerformance {
    Integer tenantId;
    String name;
    String address;
    BusinessAmount revenue;
    BusinessAmount expense;
    BusinessAmount summary;
}
