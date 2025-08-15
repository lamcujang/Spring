package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.Timesheet}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)

public class TimesheetDto implements Serializable {
    Integer id;
//    @NotNull
//    Integer orgId;
    OrgDto orgDto;
//    @NotNull
//    Integer employeeId;
    EmployeeDto employeeDto;
    @NotNull
    Integer configShiftId;
    @Size(max = 255)
    String workType;
    @Size(max = 50)
    String status;
    String checkInTime;
    String checkOutTime;
    BigDecimal totalHours;
    @Size(max = 255)
    String description;
    @Size(max = 25)
    String checkinSource;
//    Integer approvedBy;
    EmployeeDto approvedByDto;
    String approvedAt;
}