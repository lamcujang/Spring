package org.common.dbiz.request.productRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ProductComboDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.ProductLocationDto;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveAllProductAttr {

    private Integer productId;
    private Integer uomId;
    private Integer[] orgId;
    private Integer[] itemIds; // thanh phan
    private List<ProductComboDto> itemProducts;
    private Integer[] toppingIds; // mon them
    private  List <ProductDto> listProductDto;

}
