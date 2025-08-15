package org.common.dbiz.dto.paymentDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.domain.Bank}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BankDto implements Serializable {
    String isActive;
    Integer tenantId;
    Integer id;
    @NotNull
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String description;
    @Size(max = 15)
    String binCode;
    @Size(max = 15)
    String swiftCode;
    BigDecimal imageId;
    String code;
    String shortName;
    String imageUrl;

    // Napas integration status
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String requestStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String requestStatusName;


}