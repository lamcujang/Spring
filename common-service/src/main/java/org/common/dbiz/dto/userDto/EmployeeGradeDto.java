package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.EmployeeGrade}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeGradeDto implements Serializable {
    Integer id;
    Integer orgId;
    Integer tenantId;
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String code;
    String jobDescription;
    String permission;
    BigDecimal level;
    String experienceRequired;
    BigDecimal baseSalaryMin;
    String permissionName;
    String experienceRequiredName;
}