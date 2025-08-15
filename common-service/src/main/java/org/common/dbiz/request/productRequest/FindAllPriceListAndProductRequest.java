package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindAllPriceListAndProductRequest {
    private Integer orgId;

     private Integer productId;

     private Integer priceListId;

     private String productType;

     private String name;

     private Integer productCategoryId;

    private Integer categoryId;

    private String barcode;

    private int pageSize=15;
    private String order="desc";
    private String sortBy="created";
    private int page=0;
    private Integer posTerminalId;
}
