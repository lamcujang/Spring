package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.dbiz.app.productservice.domain.view.PriceListV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriceListVDto implements Serializable {
    Integer priceListId;
    Integer orgId;
    @Size(max = 64)
    String name;
    @Size(max = 1)
    String isActive;
    Instant fromDate;
    Instant toDate;
    @Size(max = 1)
    String isSaleprice;
    @Size(max = 1)
    String generalPricelist;
    Integer currencyId;
    Integer pricePrecision;
    @Size(max = 1)
    String isDefault;
    Integer posTerminalId;
}