package org.common.dbiz.dto.paymentDto;

import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.domain.PayMethod}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PayMethodAndOrgDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer id;
    Integer bankId;
    @Size(max = 255)
    String description;
    @Size(max = 255)
    String name;
    BigDecimal imageId;
    @Size(max = 1)
    String isDefault;


    PayMethodOrgDto payMethodOrgDto;
}