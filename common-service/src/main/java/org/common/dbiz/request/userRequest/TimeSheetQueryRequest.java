package org.common.dbiz.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheetQueryRequest extends BaseQueryRequest {

    private String keyword;
    private String shiftType;
    private Integer orgId;
    private String startDate;
    private String endDate;
    private Integer employeeCreatedBy;

}
