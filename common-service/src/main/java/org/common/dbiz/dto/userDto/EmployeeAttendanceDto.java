package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.EmployeeAttendance}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeAttendanceDto implements Serializable {
    Integer id;
//    Integer orgId;
//    Integer employeeId;
    String attendanceDate;
    String checkinTime;
    String checkinReason;
    Integer configShiftId;
    String attendanceType;
//    Integer approvedBy;
    OrgDto orgDto;
    EmployeeDto employeeDto;
    EmployeeDto approvedByDto;

}