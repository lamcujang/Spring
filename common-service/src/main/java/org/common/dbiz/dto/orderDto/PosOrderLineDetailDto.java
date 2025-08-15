package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.PosOrderLineDetail}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PosOrderLineDetailDto implements Serializable {
    String isActive;
    Integer id;
    Integer orgId;
    @Size(max = 255)
    String description;
    @NotNull
    Integer posOrderLineId;
    @NotNull
    Integer productId;
    private BigDecimal qty;
    String productName;
    Integer taxId;
    BigDecimal salesPrice;
    BigDecimal taxRate;
    String taxName;
    String status;
    String valueStatus;
}