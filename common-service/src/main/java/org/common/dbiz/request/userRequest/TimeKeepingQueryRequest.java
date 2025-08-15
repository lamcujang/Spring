package org.common.dbiz.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeKeepingQueryRequest extends BaseQueryRequest {
    private Integer orgId;
    private Integer id;
    String createFrom;
    String createTo;
    String checkinAddress;
    Integer employeeCreated;
    String isActive;
}
