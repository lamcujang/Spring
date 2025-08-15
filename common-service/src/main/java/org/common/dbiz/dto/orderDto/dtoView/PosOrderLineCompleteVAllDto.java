package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.domain.view.PosOrderLineCompleteVAll}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PosOrderLineCompleteVAllDto implements Serializable {
    Integer orgId;
    Integer id;
    BigDecimal salesPrice;
    BigDecimal qty;
    @Size(max = 64)
    String status;
    @Size(max = 15)
    String valueStatus;
    @Size(max = 255)
    String description;
    Integer productId;
    ProductVDto productDto;
    UomVDto uomDto;
    BigDecimal taxAmount;
    BigDecimal lineNetAmt;
    BigDecimal grandTotal;
    Integer taxId;
}