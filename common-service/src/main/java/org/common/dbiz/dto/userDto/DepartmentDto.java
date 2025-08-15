package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.Department}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentDto implements Serializable {
    Integer id;
    private Integer orgId;
    @Size(max = 1)
    String isActive;
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String code;
    String description;
    String establishedDate;
    Integer departmentHeadId;
    String departmentHeadName;

    Integer totalEmployees;
}