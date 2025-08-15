package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.ReceiptOtherOrg}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptOtherOrgDto implements Serializable {
    Integer id;
    Integer tenantId;
    Integer orgId;
    String isActive;

    String name;
    String phone;
    String wards;
    String area;
    String address;
    String code;
    String isAssign;
}