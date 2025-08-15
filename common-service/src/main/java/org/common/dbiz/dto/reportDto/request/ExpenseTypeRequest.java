package org.common.dbiz.dto.reportDto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExpenseTypeRequest extends BaseQueryRequest {
    Integer id;
    String name;
    String description;
    String indicatorCode;
}
