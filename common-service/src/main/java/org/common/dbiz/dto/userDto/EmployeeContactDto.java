package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.EmployeeContact}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeContactDto implements Serializable {
    Integer id;
    Integer employeeId;
    @Size(max = 500)
    String fullName;
    @Size(max = 50)
    String phone;
    @Size(max = 50)
    String relationship;
    @Size(max = 50)
    String contactType;
    @Size(max = 500)
    String address;
    Integer tenantId;
    Integer orgId;
}