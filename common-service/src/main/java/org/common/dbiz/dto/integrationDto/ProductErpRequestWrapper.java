package org.common.dbiz.dto.integrationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductErpRequestWrapper {
    private List<ProductErpDto> data;
}

