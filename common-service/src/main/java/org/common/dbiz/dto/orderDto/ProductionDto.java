package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.Production}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductionDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer id;
    Integer productId;
    Integer doctypeId;
    @Size(max = 32)
    String documentno;
    @Size(max = 255)
    String name;
    String movementDate;
    BigDecimal productionQty;
    @Size(max = 32)
    String documentStatus;
    @Size(max = 255)
    String description;
    @Size(max = 1)
    String isProcessed;
    @Size(max = 1)
    String isSyncErp;
    Integer erpProductionId;
    Integer warehouseId;
    Integer locatorId;
    String statusName;
    String productName;
    String warehouseName;
    String orgName;
    List<ProductionLineDto> productionLines;
}