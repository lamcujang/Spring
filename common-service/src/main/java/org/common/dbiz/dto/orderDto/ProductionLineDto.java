package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.domain.ProductionLine}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductionLineDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer id;
    Integer productionId;
    Integer lineNo;
    Integer productId;
    @Size(max = 1)
    String isEndProduct;
    BigDecimal plannedQty;
    @Size(max = 255)
    String description;
    BigDecimal usedQty;
    String uomName;
    String productName;
}