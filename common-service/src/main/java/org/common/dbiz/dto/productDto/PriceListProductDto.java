package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceListProductDto implements Serializable {
    @Size(max = 1)
    String isActive;
    Integer id;
    Integer productId;
    BigDecimal costPrice; // gia von
    BigDecimal standardPrice; // gia chung
    BigDecimal salesPrice; // gia ban , moi
    Integer priceListId;
    Integer orgId;
    BigDecimal lastOrderPrice;


    ProductDto product;
    Integer productCategoryId;

    Integer[] productIds;

    Boolean isApplyPriceNewAll;

    @JsonView(JsonViewPriceListProductDto.viewJsonSavePriceListProduct.class)
    List<ProductDto> listProduct;

    public static class IdsRequest{
         private List<Integer> ids;
        public List<Integer> getIds() {
            return ids;
        }

        public void setIds(List<Integer> ids) {
            this.ids = ids;
        }
    }
}