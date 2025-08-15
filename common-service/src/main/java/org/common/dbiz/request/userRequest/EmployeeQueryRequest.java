package org.common.dbiz.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeQueryRequest extends BaseQueryRequest {

   String keyword;
   Integer departmentId;
   Integer employeeGradeId;
    Integer orgId;
    String workStatus;
}
