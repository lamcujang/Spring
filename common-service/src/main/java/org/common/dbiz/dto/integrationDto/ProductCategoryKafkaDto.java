package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.PcTerminalAccessDto;
import org.common.dbiz.dto.productDto.ProductCategoryDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.ProductCategory}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductCategoryKafkaDto implements Serializable {
    Integer tenantId;
    String status;
    String error;
    List<ProductCategoryDto> productCategoryDtos;
}