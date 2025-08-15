package org.common.dbiz.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAttendanceQueryRequest extends BaseQueryRequest {

    private Integer orgId ;
    private Integer id;
}
