package org.common.dbiz.dto.userDto.nested;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO for {@link com.dbiz.app.userservice.domain}
 * bo khong sai
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonFilter("employeeDto")
public class ConfigShiftDto implements Serializable {
    Integer id;
    String name;
    String startTime;
    String endTime;

}