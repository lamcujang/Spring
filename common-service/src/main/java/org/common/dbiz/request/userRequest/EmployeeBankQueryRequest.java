package org.common.dbiz.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeBankQueryRequest extends BaseQueryRequest {

     private String keyword;
}
