package org.common.dbiz.dto.paymentDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.PayMethodOrg}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PayMethodOrgDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer id;
    Integer payMethodId;
    @Size(max = 32)
    String accessCode;
    @Size(max = 32)
    String terminalId;
    @Size(max = 32)
    String merchantCode;
    @Size(max = 512)
    String hashKey;
    @Size(max = 255)
    String description;
    
}