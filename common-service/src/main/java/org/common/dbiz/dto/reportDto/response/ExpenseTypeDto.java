package org.common.dbiz.dto.reportDto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExpenseTypeDto {
    Integer id;
    Integer orgId;
    String name;
    String description;
    String indicatorCode;
}