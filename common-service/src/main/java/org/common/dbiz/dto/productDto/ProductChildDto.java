package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductChildDto implements Serializable {
    Integer id;

    //    @JsonProperty("d_product_category_id")
//    @NotNull(message = "Product category cannot be null")

    ProductCategoryDto productCategory;

    String code;

    String name;

    String qrcode;
    BigDecimal saleprice;
    BigDecimal costprice;
    UomDto uom;
    BigDecimal onHand;

    String isPurchased;


    String attribute1;

    String nameAttr2;
    String attribute2;

    String nameAttr3;
    String attribute3;

    String nameAttr4;
    String attribute4;

    String nameAttr5;
    String attribute5;

    String nameAttr6;
    String attribute6;

    String nameAttr7;
    String attribute7;

    String nameAttr8;
    String attribute8;

    String nameAttr9;
    String attribute9;

    String nameAttr10;
    String attribute10;

    String description;

    ImageDto image;

//    @NotNull(message = "Organization cannot be null")
    Integer orgId;

    String isTopping;

    BigDecimal minOnHand;

    BigDecimal maxOnHand;

    String isActive;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Integer taxId;

    TaxDto tax;

    String productType;

    String groupType;

    Integer productParentId;

    String locatorId;

    BigDecimal qtyConversion;

    BigDecimal weight;

    String brand;

    BigDecimal qty;

    List<AssignOrgProductDto> assignOrgProductDtos;
    ProductLocationDto listProductLocation;

}