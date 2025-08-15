package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.userDto.nested.DepartmentDto;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.ConfigShiftEmployee}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigShiftEmployeeDto implements Serializable {
    Integer id;
//    Integer orgId;
    Integer tenantId;
    Integer configShiftId;
    Integer employeeId;
    String isActive;
//    String orgName;
//    String  employeeName;
//    String departmentName;
    OrgDto orgDto;
    EmployeeDto employeeDto;
    DepartmentDto departmentDto;
    String workStatusName;
}