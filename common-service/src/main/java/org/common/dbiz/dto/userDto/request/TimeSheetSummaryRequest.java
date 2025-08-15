package org.common.dbiz.dto.userDto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TimeSheetSummaryRequest extends BaseQueryRequest {
    Integer id;
    Integer employeeId;
    String employeeKeyword;
    Integer orgId;
    Integer departmentId;
    Integer employeeGradeId;
    String employeeType;
    String month;
}
