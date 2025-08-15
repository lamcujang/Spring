package org.common.dbiz.dto.productDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SaveProductLocationDto {

    Integer productId;
    String isFirstCreation;
    List<ProductLocationDto> listProductLocation;
}
