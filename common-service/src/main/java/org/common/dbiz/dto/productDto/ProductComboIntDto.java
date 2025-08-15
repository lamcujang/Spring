package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductComboIntDto implements Serializable {
    Integer tenantId;
    Integer orgId;
    @Size(max = 1)
    String isActive;
    Integer id;
    Integer productId;
    @Size(max = 1)
    String isItem;
    BigDecimal qty;
    Integer productComponentId;
    ProductCDto component;
    private Integer sequence;
}