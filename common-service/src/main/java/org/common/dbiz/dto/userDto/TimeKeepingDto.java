package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.TimeKeeping}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)

public class TimeKeepingDto implements Serializable {
    Integer id;
    @Size(max = 500)
    String checkinAddress;
    @Size(max = 500)
    String gpsCoordinates;
    Integer checkinRadiusMeters;
    @Size(max = 50)
    String isActive;
    String updatedQr;
    @JsonIgnore
    EmployeeDto employeeCreatedByDto;
    OrgDto orgDto;
}