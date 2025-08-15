package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceListAndProductDto {

//    private PricelistDto pricelistDto;
    private ProductDto productDto;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PriceListProductDto priceListProductDto;

    public PriceListAndProductDto(ProductDto productDto, PriceListProductDto priceListProductDto) {
        this.productDto = productDto;
        this.priceListProductDto = priceListProductDto;
    }
}
