package org.common.dbiz.dto.reportDto.response.revenue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RevenueBusiness {
    private List<InvoiceHouseHoldDto> raws;
    private Integer tenantId;
    private String name;
    private String address;
    private List<RevenueChildDTO> summaryAmount;
}
