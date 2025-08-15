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
public class ProductIntDto implements Serializable {
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

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    String attribute6;

    String attribute7;

    String attribute8;

    String attribute9;

    String attribute10;

    String description;

    ImageDto image;

//  @NotNull(message = "Organization cannot be null")
    Integer orgId;

    String isTopping;

    BigDecimal minOnHand;

    BigDecimal maxOnHand;

    String isActive;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Integer taxId;

    TaxCategoryIntDto taxCategory;

    String productType;

    String groupType;

    Integer productParentId;

    String locatorId;

    BigDecimal qtyConversion;

    BigDecimal weight;

    String brand;

    BigDecimal qty;

    String isSales;

    String isPurchase;

    String isStocked;

    Integer erpProductId;
    private BigDecimal preparationTime;
    private BigDecimal cookingTime;


    List<AssignOrgProductDto> assignOrg;

    List<ProductLocationDto> productLocation;

    List<ProductChildDto> productAttributes;

    List<ProductComboIntDto> productCombo;

}