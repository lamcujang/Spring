package org.common.dbiz.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceQueryRequest extends BaseQueryRequest {

    private String keyword;
    private Integer orgId;
    private String fromDate;
    private String toDate;
    private String timeKeepingType;
}
