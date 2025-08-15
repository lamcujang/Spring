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
public class InvoiceHouseHoldDto {
    private String invoiceCode;
    private String invoiceDate;
    private String invoiceDescription;
    private List<RevenueGroupDto> revenue;
}
