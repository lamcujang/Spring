package org.common.dbiz.dto.integrationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ProductCategoryDto;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductCatIntDto {
    String type;
    List<ProductCategoryDto> productCategoryDtos;
}
