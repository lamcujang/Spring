package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.userservice.domain.EmployeeBank}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeBankDto implements Serializable {
    Integer id;
    Integer orgId;
    Integer tenantId;
    Integer employeeId;
    @Size(max = 20)
    String accountType;
    Integer bankAccountId;
    @Size(max = 1)
    String isDefault;
    Integer bankId;
    String branch;
    String accountNo;
    String name;
}