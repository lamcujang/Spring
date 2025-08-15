package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.ConfigShift}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigShiftDto implements Serializable {
    Integer id;
//    Integer orgId;
    String isActive;
    @Size(max = 255)
    String code;
    @Size(max = 255)
    String name;
    @Size(max = 50)
    String shiftType;
    String startTime;
    Integer breakDurationMinutes;
    String endTime;
    String checkinTime;
    String checkoutTime;
    String validFrom;
    String validTo;
    @Size(max = 50)
    String[] workingDays;
    @Size(max = 1)
    String isValidTo;
//    Integer employeeCreatedBy;
//    String orgName;
//    String employeeCreatedByName;
    EmployeeDto employeeCreatedDto;
    OrgDto orgDto;
    String shiftTypeName;
    List<ConfigShiftEmployeeDto> configShiftEmployees;
}