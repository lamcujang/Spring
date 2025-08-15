package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.userDto.nested.ConfigShiftDto;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.AttendanceRequest}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)

public class AttendanceRequestDto implements Serializable {
    Integer id;
    @Size(max = 50)
    String timeKeepingType;
    String timeKeepingTypeName;
//    @NotNull
//    Integer configShiftId;
    ConfigShiftDto configShiftDto;
    @Size(max = 255)
    String reasonRequest;
    @Size(max = 50)
    String status;
    String created;
//    Integer employeeId;
    EmployeeDto employeeDto;
//    Integer orgId;
   OrgDto orgDto;
}
