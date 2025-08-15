package org.common.dbiz.dto.productDto.otherServiceDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.productDto.ProductCDto;
import org.common.dbiz.dto.productDto.ProductComboDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.ProductERequestV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductERequestVDto implements Serializable {
    Integer productId;
    Integer orgId;
    Integer productCategoryId;
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String imageUrl;
    Integer posTerminalId;
    BigDecimal salesPrice;
    @Size(max = 1)
    String isActive;
    Integer taxId;
    List<ProductCDto> components;
    List<ProductCDto> extraItems;
}