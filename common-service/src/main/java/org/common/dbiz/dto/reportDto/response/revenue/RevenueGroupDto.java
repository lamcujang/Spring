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
public class RevenueGroupDto {
    private String code;
    private String nameGroup;
    private List<RevenueChildDTO> child;
}
