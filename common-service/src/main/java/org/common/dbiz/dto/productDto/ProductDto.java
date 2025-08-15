package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto implements Serializable {

    Integer id;

    //    @JsonProperty("d_product_category_id")
//    @NotNull(message = "Product category cannot be null")

    ProductCategoryDto productCategory;

    String code;

    String name;

    String nameWithTax;

    String qrcode;
    BigDecimal saleprice;
    BigDecimal costprice;
    UomDto uom;
    BigDecimal onHand;

    BusinessSectorDto businessSector;

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

//    @NotNull(message = "Organization cannot be null")
    Integer orgId;

    String isTopping;

    BigDecimal minOnHand;

    BigDecimal maxOnHand;

    String isActive;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
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


    String isSales;
    String isStocked;

    String sku;
    String barcode;
    Integer brandId;
    BigDecimal height;
    BigDecimal width;
    BigDecimal length;
    String isExpiryWarning;
    Integer expiryWarningDay;
    String isAllowComExport;
    String weightUom;
    String specUom;
    BigDecimal preparationTime;
    BigDecimal cookingTime;

    Integer erpProductId;

    Integer priceListId;

    List<ProductChildDto> productAttributes;

    List<AssignOrgProductDto> assignOrgProductDtos;

    ProductLocationDto listProductLocation;

    Integer inventoryCategorySpecialTaxId;

    String declaredTaxReduction;
}