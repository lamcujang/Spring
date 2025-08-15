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
public class OverTimeLogRequest extends BaseQueryRequest {
    Integer id;
    Integer employeeId;
}
