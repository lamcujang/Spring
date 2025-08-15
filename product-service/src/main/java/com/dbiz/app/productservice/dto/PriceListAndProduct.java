package com.dbiz.app.productservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.dbiz.app.productservice.domain.PriceListProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.PriceListProductDto;
import org.common.dbiz.dto.productDto.ProductDto;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
public class PriceListAndProduct {

//    private PricelistDto pricelistDto;
    private ProductDto productDto;
    private String name;
    private String taxName;
    private BigDecimal taxRate;
    private Integer taxId;
    private Integer productId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PriceListProduct priceListProductDto;


}
