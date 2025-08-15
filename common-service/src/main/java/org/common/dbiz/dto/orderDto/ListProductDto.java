package org.common.dbiz.dto.orderDto;


import lombok.*;
import org.common.dbiz.dto.orderDto.response.ProductDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ListProductDto {

    List<ProductDto> products;
}
