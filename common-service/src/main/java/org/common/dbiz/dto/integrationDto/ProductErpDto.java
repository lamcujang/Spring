package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.UomDto;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductErpDto {
    String value;
    String name;
    String productType;
    Integer posTerminalId ;

    @JsonProperty("mProductCataegoryId")
    Integer mProductCataegoryId;

    BigDecimal salesPrice;
    Integer taxCategoryId;
    Integer unShowPos;
//    Integer ad_Client_ID;
    Integer isAuxilira;
    String isStocked;
    String description;
    Integer ad_Org_ID;
    Integer productId;
    List<Integer> listOrgIds;
    UomErpDto uom;
    List<ProductComponentErpDto> productBoms;
    List<ProductComponentErpDto> productExtra;
    List<ProductLocationErpDto> productLocations;
}
