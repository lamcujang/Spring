package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.productservice.domain.ProductLocation}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductLocationDto implements Serializable {
    String isActive;
    Integer id;
    Integer orgId;
    Integer warehouseId;
    Integer posTerminalId;
    Integer productId;
    Integer erpProductLocationId;
    @Size(max = 1)
    String isDefault;
    Integer locatorId;
    BigDecimal stockQty;
    BigDecimal minQty;
    BigDecimal maxQty;
    String orgName;
    String nameWarehouse;
    String isSysDefault;

}