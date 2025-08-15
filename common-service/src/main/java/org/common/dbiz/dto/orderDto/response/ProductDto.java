package org.common.dbiz.dto.orderDto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDto implements Serializable {
    Integer id;
    String code;
    String name;
    String productType;
    BigDecimal onHand;
    private BigDecimal preparationTime;
    private BigDecimal cookingTime;


}
